package com.daedongmap.daedongmap.user.controller;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.CustomUserDetails;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.service.TokenService;
import com.daedongmap.daedongmap.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/refresh-token")
    @Operation(summary = "access 토큰 새로 발급", description = "기존의 access 토큰 기한이 지난 경우 refresh token 전송받아 인증 절차 후 새로운 access token 발급")
    public ResponseEntity<JwtTokenDto> refreshToken(HttpServletRequest request) {

        String refreshToken = request.getHeader("Authorization");

        Long userId = tokenService.validate(refreshToken);

        Users user = userService.findUser(userId);

        JwtTokenDto newAccessToken = tokenProvider.createNewAccessToken(user, user.getRoles());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newAccessToken);
    }

    // 헤더 토큰 정보로 로그인한 유저 아이디 조회
    // 헤더의 토큰 정보로 조회 - 인증, 인가 과정 불필요
    @PostMapping("/user/current")
    @Operation(summary = "현재 로그인한 유저의 아이디 조회")
    public Long userIdFromToken(@AuthenticationPrincipal CustomUserDetails user) {

        if(user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else {
            return user.getUser().getId();
        }
    }
}
