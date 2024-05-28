package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.s3.S3Service;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import com.daedongmap.daedongmap.user.dto.response.AuthResponseDto;
import com.daedongmap.daedongmap.user.dto.response.UserResponseDto;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    S3Service s3Service;

    UserRegisterDto userRegisterDto = UserRegisterDto.builder()
            .nickName("fake user")
            .email("test@test.com")
            .password("12345678")
            .phoneNumber("01015771577")
            .build();

    @Test
    @Order(1)
    @DisplayName("사용자 등록")
    void registerUser() {
        // given
        Users user = createFakeUser(userRegisterDto);
        ReflectionTestUtils.setField(user, "id", 11L);

        // when
        given(userRepository.save(any())).willReturn(user);

        AuthResponseDto authResponseDto = userService.registerUser(userRegisterDto);

        // then
        assertEquals(user.getNickName(), authResponseDto.getNickName());
    }

    @Test
    @Order(2)
    @DisplayName("비밀번호 암호화 테스트")
    void encodePassword() {
        // given
        String password = "random password";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        assertEquals(passwordEncoder.encode(password), encodedPassword);
    }

    @Test
    @Order(3)
    @DisplayName("사용자 로그인 테스트")
    void loginUser() {
        // given
        Users user = createFakeUser(userRegisterDto);
        userRepository.save(user);

        UserLoginDto userLoginDto = new UserLoginDto(
                "test@test.com",
                "12345678");

        // when
        given(userRepository.findByEmail(userLoginDto.getEmail()))
                .willReturn(Optional.ofNullable(user));
        String password = userRepository.findByEmail(userLoginDto.getEmail()).get().getPassword();

        // then
        assertEquals(passwordEncoder.encode(userLoginDto.getPassword()), password);
    }

    @Test
    @Order(4)
    @DisplayName("사용자 아이디 찾기 테스트")
    void retrieveUserId() {
        // given
        Users user = createFakeUser(userRegisterDto);

        // when
        userRepository.save(user);
        given(userRepository.findByPhoneNumber(user.getPhoneNumber()))
                .willReturn(Optional.of(user));

        // then
        String email = userService.retrieveUserId(user.getPhoneNumber());
        assertEquals(user.getEmail(), email);
    }

    @Test
    @Order(5)
    @DisplayName("사용자 정보 반환")
    void returnUserDtoById() {
        // given
        Users user = createFakeUser(userRegisterDto);

        // when
        userRepository.save(user);
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        // then
        UserResponseDto userResponseDto = userService.returnUserDtoById(user.getId());
        assertEquals(user.getEmail(), userResponseDto.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("사용자 업데이트 테스트")
    void updateUser() throws IOException {
        // given
        Users user = createFakeUser(userRegisterDto);
        UserUpdateDto userUpdateDto = new UserUpdateDto(
                "update test",
                "update status",
                null,
                null);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());

        // when
        userRepository.save(user);
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        // then
        UserResponseDto userResponseDto = userService.updateUser(user.getId(), multipartFile, userUpdateDto);
        assertEquals(userUpdateDto.getNickName(), userResponseDto.getNickName());


    }

    @Test
    @Order(7)
    @DisplayName("사용자 삭제 테스트")
    void deleteUser() {
        // given
        Users user = createFakeUser(userRegisterDto);

        // when
        userRepository.save(user);
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        // then
        String nickName = userService.deleteUser(user.getId());
        assertEquals(user.getNickName(), nickName);

    }

    private Users createFakeUser(UserRegisterDto userRegisterDto) {
        return Users.builder()
                .nickName(userRegisterDto.getNickName())
                .status("test status")
                .email(userRegisterDto.getEmail())
                .webSite("test website")
                .profileImage(userRegisterDto.getProfileImage())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .profileImage("test image")
                .password(userService.encodePassword(userRegisterDto.getPassword()))
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();
    }
}