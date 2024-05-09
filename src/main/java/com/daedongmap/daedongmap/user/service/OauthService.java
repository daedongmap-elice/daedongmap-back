package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.security.OauthConfig.GoogleConfig;
import com.daedongmap.daedongmap.security.OauthConfig.KakaoConfig;
import com.daedongmap.daedongmap.security.OauthConfig.NaverConfig;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final NaverConfig naverConfig;
    private final KakaoConfig kakaoConfig;
    private final GoogleConfig googleConfig;

    private String TYPE;
    private String clientId;
    private String redirectUri;
    private String authURL;
    private String infoURL;
    private String email;
    private String nickName;


    @Transactional
    public JwtTokenDto signUpAndLogin(String code, String type) {

        TYPE = type;

        switch (TYPE) {
            case "naver" -> {
                clientId = naverConfig.getClientId();
                redirectUri = naverConfig.getRedirectUri();
            }
            case "kakao" -> {
                clientId = kakaoConfig.getClientId();
                redirectUri = kakaoConfig.getRedirectUri();
            }
            case "google" -> {
                clientId = googleConfig.getClientId();
                redirectUri = googleConfig.getRedirectUri();
            }
        }

        OAuthUserInfoDto profile = getUserProfile(getToken(code));
        Users user = userService.findUserByEmail(profile.getEmail());

        if(user != null && !user.getIsMember()) {
            return tokenProvider.createToken(user, user.getRoles());
        }

        UserRegisterDto newUser = UserRegisterDto.builder()
                .nickName(profile.getNickName())
                .email(profile.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .profileImage(profile.getProfileImage())
                .build();

        Users createdUser = userService.registerUser(newUser).getUser();
        return tokenProvider.createToken(createdUser, createdUser.getRoles());
    }

    @Transactional
    public OAuthTokenResponseDto getToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        switch (TYPE) {
            case "naver" -> {
                body.add("grant_type", "authorization_code");
                body.add("client_id", clientId);
                body.add("client_secret", naverConfig.getClientSecret());
                body.add("redirect_uri", redirectUri);
                body.add("code", code);
                authURL = naverConfig.getTokenUri();
            }
            case "kakao" -> {
                body.add("grant_type", "authorization_code");
                body.add("client_id", clientId);
                body.add("code", code);
                body.add("redirect_uri", redirectUri);
                authURL = kakaoConfig.getTokenUri();
            }
            case "google" -> {
                body.add("grant_type", "authorization_code");
                body.add("client_id", clientId);
                body.add("client_secret", googleConfig.getClientSecret());
                body.add("redirect_uri", redirectUri);
                body.add("code", code);
                authURL = googleConfig.getTokenUri();
            }
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
            if(TYPE.equals("google")) {
                return OAuthTokenResponseDto.builder()
                        .accessToken(jsonNode.get("access_token").asText())
                        .build();
            }
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

        switch (TYPE) {
            case "naver" -> {
                infoURL = naverConfig.getUserInfoUri();
                email = "email";
                nickName = "nickname";
            }
            case "kakao" -> {
                infoURL = kakaoConfig.getUserInfoUri();
                email = "account_email";
                nickName = "profile_nickname";
            }
            case "google" -> infoURL = googleConfig.getUserInfoUri();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        if(TYPE.equals("google")) {
            response = restTemplate.exchange(
                    infoURL,
                    HttpMethod.GET,
                    googleRequest,
                    String.class
            );
        } else {
            response = restTemplate
                    .postForEntity(infoURL, request, String.class);
        }

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
            }

            if(TYPE.equals("kakao")) {
                return OAuthUserInfoDto.builder()
                        .email(String.valueOf(jsonNode.get("kakao_account").get("email")).replaceAll("\"", ""))
                        .nickName(String.valueOf(jsonNode.get("kakao_account").get("profile").get("nickname")).replaceAll("\"", ""))
                        .profileImage(String.valueOf(jsonNode.get("properties").get("profile_image")).replaceAll("\"", ""))
                        .build();
            }

            if(TYPE.equals("google")) {
                return OAuthUserInfoDto.builder()
                        .email(String.valueOf(jsonNode.get("email")).replaceAll("\"", ""))
                        .nickName(String.valueOf(jsonNode.get("name")).replaceAll("\"", ""))
                        .profileImage(String.valueOf(jsonNode.get("picture")).replaceAll("\"", ""))
                        .build();
            }
        } catch(Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return null;
    }
}
