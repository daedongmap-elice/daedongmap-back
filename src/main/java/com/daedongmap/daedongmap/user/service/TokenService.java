package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.RefreshTokens;
import com.daedongmap.daedongmap.user.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;


    @Transactional
    public String deleteByUserId(Long id) {

        try {
            tokenRepository.deleteByUserId(id);
        } catch (Exception e) {
            return "해당 사용자에 대한 refresh 토큰이 존재하지 않습니다.";
        }

        return "기존 사용자에 대한 refresh 토큰 삭제 완료";
    }

    @Transactional
    public String save(RefreshTokens toSaveToken) {
        
        tokenRepository.save(toSaveToken);

        return "refresh 토큰 저장 완료";
    }

    public Long validate(String refreshToken) {

        String token = "";

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            token = refreshToken.substring(7);
        }

        RefreshTokens foundToken = tokenRepository.findByRefreshToken(token).orElseThrow(() ->
                new CustomException(ErrorCode.LOGIN_REQUIRED));

        return foundToken.getUserId();
    }
}
