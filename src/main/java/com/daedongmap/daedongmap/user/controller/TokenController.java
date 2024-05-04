package com.daedongmap.daedongmap.user.controller;

import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.service.Facade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final Facade facade;

    @GetMapping("/refresh-token")
    @Operation(summary = "access 토큰 새로 발급", description = "기존의 access 토큰 기한이 지난 경우 refresh token 전송받아 인증 절차 후 새로운 access token 발급")
    public ResponseEntity<JwtTokenDto> refreshToken(HttpServletRequest request) {

        String refreshToken = request.getHeader("Authorization");
        JwtTokenDto newAccessToken = facade.getJwtTokenDto(refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newAccessToken);
    }
}
