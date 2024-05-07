package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceFacade {

    private final TokenService tokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Transactional
    public JwtTokenDto getJwtTokenDto(String refreshToken) {

        Users foundUser = userService.findUserById(tokenService.validate(refreshToken));

        return tokenProvider.createNewAccessToken(foundUser, foundUser.getRoles());
    }

    @Transactional
    public String logoutUser(String refreshToken) {

        Long userId = tokenService.validate(refreshToken);

        return tokenService.deleteRefreshByUserId(userId);
    }

    @Transactional
    public String deleteUser(Long id) {

        try {
            tokenService.deleteRefreshByUserId(id);
            return userService.deleteUser(id);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
