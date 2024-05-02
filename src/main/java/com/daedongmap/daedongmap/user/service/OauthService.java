package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.dto.response.OAuthTokenResponseDto;
import com.daedongmap.daedongmap.user.dto.response.OAuthUserInfoDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT;

    @Value("${spring.security.oauth2.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;

    @Value("${spring.security.oauth2.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT;

    @Value("${spring.security.oauth2.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${spring.security.oauth2.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO;

    private String TYPE;
    private String clientId;
    private String redirectUri;
    private String authURL;
    private String infoURL;

    @Transactional
    public JwtTokenDto signUpAndLogin(String code, String type) {

        TYPE = type;

        if(TYPE.equals("naver")) {
            clientId = NAVER_CLIENT_ID;
            redirectUri = NAVER_REDIRECT;
        } else if(TYPE.equals("kakao")) {
            clientId = KAKAO_CLIENT_ID;
            redirectUri = KAKAO_REDIRECT;
        }

        OAuthTokenResponseDto token = getToken(code);

        OAuthUserInfoDto profile = getUserProfile(token);

        Users user = userService.findUserByEmail(profile.getEmail());

        if(user != null) {
            return tokenProvider.createToken(user, user.getRoles());
        }

        UserRegisterDto newUser = UserRegisterDto.builder()
                .nickName(profile.getNickName())
                .email(profile.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .profileImage(profile.getProfileImage())
                .build();

        userService.registerUser(newUser);

        Users foundUser = userService.findUserByEmail(profile.getEmail());

        return tokenProvider.createToken(foundUser, foundUser.getRoles());
    }

    @Transactional
    public OAuthTokenResponseDto getToken(String code) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        if(TYPE.equals("naver")) {
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("client_secret", NAVER_CLIENT_SECRET);
            body.add("redirect_uri", redirectUri);
            body.add("code", code);
            authURL = NAVER_TOKEN_URI;
        } else if(TYPE.equals("kakao")) {
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("code", code);
            body.add("redirect_uri", redirectUri);
            authURL = KAKAO_TOKEN_URI;
        }


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response= restTemplate.exchange(
                authURL,
                HttpMethod.POST,
                request,
                String.class);

        String responseBody = response.getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return OAuthTokenResponseDto.builder()
                    .accessToken(jsonNode.get("access_token").asText())
                    .refreshToken(jsonNode.get("refresh_token").asText())
                    .build();

        } catch(Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Transactional
    private OAuthUserInfoDto getUserProfile(OAuthTokenResponseDto token) {

        String email = "";
        String nickName = "";

        if(TYPE.equals("naver")) {
            infoURL = NAVER_USER_INFO;
            email = "email";
            nickName = "nickname";
        } else if(TYPE.equals("kakao")) {
            infoURL = KAKAO_USER_INFO;
            email = "account_email";
            nickName = "profile_nickname";
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token.getAccessToken());

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate
                .postForEntity(infoURL, request, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            System.out.println(jsonNode);

            if(TYPE.equals("naver")) {
                return OAuthUserInfoDto.builder()
                        .email(String.valueOf(jsonNode.get("response").get(email)).replaceAll("\"", ""))
                        .nickName(String.valueOf(jsonNode.get("response").get(nickName)).replaceAll("\"", ""))
                        .phoneNumber(String.valueOf(jsonNode.get("response").get("mobile")).replaceAll("\"", ""))
                        .profileImage(String.valueOf(jsonNode.get("response").get("profile_image")).replaceAll("\"", ""))
                        .build();
            } else if(TYPE.equals("kakao")) {
                return OAuthUserInfoDto.builder()
                        .email(String.valueOf(jsonNode.get("kakao_account").get("email")).replaceAll("\"", ""))
                        .nickName(String.valueOf(jsonNode.get("kakao_account").get("profile").get("nickname")).replaceAll("\"", ""))
                        .profileImage(String.valueOf(jsonNode.get("properties").get("profile_image")).replaceAll("\"", ""))
                        .build();
            } else {
                return null;
            }
        } catch(Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
