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
    public String deleteRefreshByUserId(Long id) {

        try {
            tokenRepository.deleteByUserId(id);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
        return "안전하게 로그아웃 하셨습니다.";
    }

    @Transactional
    public Boolean save(RefreshTokens toSaveToken) {
        
        try {
            tokenRepository.save(toSaveToken);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Long validate(String refreshToken) {

        String token = "";
        System.out.println(refreshToken);

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            token = refreshToken.substring(7);
        }

        System.out.println(token);

        RefreshTokens foundToken = tokenRepository.findByRefreshToken(token).orElseThrow(() ->
                new CustomException(ErrorCode.LOGIN_REQUIRED));

        return foundToken.getUserId();
    }
}
