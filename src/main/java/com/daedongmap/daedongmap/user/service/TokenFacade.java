package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenFacade {

    private final TokenService tokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public JwtTokenDto getJwtTokenDto(String refreshToken) {
        Users foundUser = userService.findUserById(tokenService.validate(refreshToken));

        return tokenProvider.createNewAccessToken(foundUser, foundUser.getRoles());
    }
}
