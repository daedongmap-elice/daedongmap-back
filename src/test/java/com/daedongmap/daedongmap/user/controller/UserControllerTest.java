package com.daedongmap.daedongmap.user.controller;

import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserFindIdDto;
import com.daedongmap.daedongmap.user.dto.request.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import com.daedongmap.daedongmap.user.repository.TokenRepository;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    UserRegisterDto userRegisterDto = UserRegisterDto.builder()
            .email("test@test.com")
            .nickName("test nickname")
            .phoneNumber("01015771577")
            .password("12345678")
            .profileImage("test profile")
            .build();

    Users testUser = Users.builder()
            .id(9L)
            .nickName(userRegisterDto.getNickName())
            .status("안녕하세요! 반갑습니다!")
            .email(userRegisterDto.getEmail())
            .webSite("아직 연결된 외부 사이트가 없습니다.")
            .phoneNumber(userRegisterDto.getPhoneNumber())
            .profileImage(userRegisterDto.getProfileImage())
            .password("$2a$10$P04N91fpnmwXfiK6JA/OiOE/P/9UDRn4mFn2sJpTWsG4bc1WDTKy.")
            .isMember(true)
            .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
            .build();

    UserLoginDto loginDto = new UserLoginDto(userRegisterDto.getEmail(), userRegisterDto.getPassword());


    @Test
    @Order(0)
    @DisplayName("사용자 등록 테스트")
    void register() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(userRegisterDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        assertEquals(userRegisterDto.getNickName(), result.getResponse().getContentAsString());
    }

    @Test
    @Order(1)
    @DisplayName("사용자 등록 테스트 (중복 이메일 실패)")
    void registerFailDuplicateEmail() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(userRegisterDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.EMAIL_IN_USE.getMessage(), result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    @DisplayName("사용자 등록 테스트 (중복 휴대전화 번호 실패)")
    void registerFailDuplicatePhone() throws Exception {
        // given
        UserRegisterDto registerDto = UserRegisterDto.builder()
                .email("newTest@test.com")
                .nickName("test nickname")
                .phoneNumber("01015771577")
                .password("12345678")
                .profileImage("test profile")
                .build();

        String json = objectMapper.writeValueAsString(registerDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.PHONE_IN_USE.getMessage(), result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    @DisplayName("사용자 로그인 테스트")
    void doLogin() throws Exception {
        // given
        // 로그인만 테스트할 경우 주석을 풀고 진행, 순서대로 할 경우 등록 테스트에서 테스트 유저를 등록
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isAccepted())
            .andReturn();
        int userId = JsonPath.read(result.getResponse().getContentAsString(), "$.userId");

        // then
        assertEquals(11, userId);
    }

    @Test
    @Order(4)
    @DisplayName("사용자 로그인 테스트 (비밀번호 에러)")
    void loginFail() throws Exception {
        // given
        loginDto.setPassword("10011001");
        String json = objectMapper.writeValueAsString(loginDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.INVALID_PASSWORD.getMessage(), result.getResponse().getContentAsString());

        loginDto.setPassword("12345678");
    }

    @Test
    @Order(5)
    @DisplayName("사용자 로그아웃 테스트")
    void logoutUser() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(post("/api/user/logout").header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals("안전하게 로그아웃 하셨습니다.", response.getResponse().getContentAsString());
    }

    // 수정 필요
//    @Test
//    @Order(6)
//    @DisplayName("사용자 로그아웃 테스트 (사용자 실패)")
//    void logoutUserFail() throws Exception {
//        // when
//        String json = objectMapper.writeValueAsString(loginDto);
//        MvcResult result = mockMvc.perform(post("/api/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isAccepted())
//                .andReturn();
//
//        String token = objectMapper.writeValueAsString(
//                JsonPath.read(
//                        result.getResponse().getContentAsString(),
//                        "$.accessToken"));
//
//        token = "Bearer " + token.replaceAll("\"", "");
//
//
//
//        // given
//        tokenRepository.deleteByUserId(11L);
//
//        MvcResult response = mockMvc.perform(post("/api/user/logout")
//                        .header("Authorization", token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        // then
//        assertEquals(ErrorCode.TOKEN_ERROR.getMessage(), response.getResponse().getContentAsString());
//    }

    @Test
    @Order(7)
    @DisplayName("사용자 아이디 찾기")
    void retrieveUserId() throws Exception {
        // given
//        userRepository.save(testUser);
        UserFindIdDto userFindIdDto = new UserFindIdDto("01015771577");
        String json = objectMapper.writeValueAsString(userFindIdDto);

        // when
        MvcResult response = mockMvc.perform(post("/api/accountId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals("test@test.com", response.getResponse().getContentAsString());
    }

    @Test
    @Order(8)
    @DisplayName("사용자 아이디 찾기 (실패)")
    void retrieveUserFail() throws Exception {
        // given
        UserFindIdDto userFindIdDto = new UserFindIdDto("01092837182");
        String json = objectMapper.writeValueAsString(userFindIdDto);

        // when
        MvcResult result = mockMvc.perform(post("/api/accountId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), result.getResponse().getContentAsString());
    }

    @Test
    @Order(9)
    @DisplayName("현재 로그인한 유저 아이디 반환")
    void returnLoggedInUserId() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(post("/api/user")
                        .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(11, Integer.parseInt(response.getResponse().getContentAsString()));
    }

    @Test
    @Order(10)
    @DisplayName("현재 로그인한 유저 아이디 반환 (실패)")
    void returnLoggedInUserIdFail() throws Exception {
        // given
        String token = "fake token";

        // when
        MvcResult response = mockMvc.perform(post("/api/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.INVALID_TOKEN.getMessage(), JsonPath.read(response.getResponse().getContentAsString(), "$.message"));
    }

    @Test
    @Order(11)
    @DisplayName("다른 사용자의 정보 조회")
    void findOtherUserById() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(get("/api/user/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        // 한글 깨지는 이유 찾아보기
        assertEquals("gildong@naver.com", JsonPath.read(response.getResponse().getContentAsString(), "$.email"));
    }

    @Test
    @Order(12)
    @DisplayName("다른 사용자의 정보 조회 (유효하지 않은 토큰 실패)")
    void findOtherUserByIdFail() throws Exception {
        // given
        String token = "wrong token";

        // when
        MvcResult response = mockMvc.perform(get("/api/user/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.INVALID_TOKEN.getMessage(), JsonPath.read(response.getResponse().getContentAsString(), "$.message"));
    }

    @Test
    @Order(13)
    @DisplayName("다른 사용자의 정보 조회 (존재하지 않는 사용자 실패)")
    void findCurrentByIdFailByUser() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(get("/api/user/20")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), response.getResponse().getContentAsString());
    }

    @Test
    @Order(14)
    @DisplayName("현재 로그인한 사용자 정보 조회")
    void findCurrentById() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(get("/api/user")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(testUser.getEmail(), JsonPath.read(response.getResponse().getContentAsString(), "$.email"));
    }

    @Test
    @Order(15)
    @DisplayName("현재 로그인한 사용자 정보 조회 (유효하지 않은 토큰 실패)")
    void findCurrentByIdFail() throws Exception {
        // given
        String token = "wrong token";

        // when
        MvcResult response = mockMvc.perform(get("/api/user/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertEquals(ErrorCode.INVALID_TOKEN.getMessage(), JsonPath.read(response.getResponse().getContentAsString(), "$.message"));
    }

    @Test
    @Order(16)
    @DisplayName("사용자 업데이트")
    void updateUser() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        UserUpdateDto updateDto = new UserUpdateDto("updated name", "updated status", null, null);
        String dto = objectMapper.writeValueAsString(updateDto);

        MockMultipartFile file = new MockMultipartFile("file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World".getBytes());

        MockMultipartFile userUpdateDto = new MockMultipartFile("userUpdateDto",
                "userUpdateDto",
                String.valueOf(MediaType.APPLICATION_JSON),
                dto.getBytes());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/user");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        // when
        MvcResult response = mockMvc.perform(builder
                        .file(file)
                        .file(userUpdateDto)
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        assertEquals(updateDto.getNickName(), JsonPath.read(response.getResponse().getContentAsString(), "$.nickName"));
    }

    @Test
    @Order(17)
    @DisplayName("사용자 삭제")
    void deleteUser() throws Exception {
        // given
//        userRepository.save(testUser);

        String json = objectMapper.writeValueAsString(loginDto);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted())
                .andReturn();

        String token = objectMapper.writeValueAsString(
                JsonPath.read(
                        result.getResponse().getContentAsString(),
                        "$.accessToken"));

        token = "Bearer " + token.replaceAll("\"", "");

        // when
        MvcResult response = mockMvc.perform(delete("/api/user")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals("updated name", response.getResponse().getContentAsString());
    }
}