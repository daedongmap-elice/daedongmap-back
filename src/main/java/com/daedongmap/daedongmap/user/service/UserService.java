package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.AuthResponse;
import com.daedongmap.daedongmap.user.dto.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.UserUpdateDto;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public AuthResponse registerUser(UserRegisterDto userRegisterDto) {

        userRepository.findByEmail(userRegisterDto.getEmail()).ifPresent(e -> {
            throw new CustomException(ErrorCode.EMAIL_IN_USE);
        });

        Users newUsers = Users.builder()
                .nickName(userRegisterDto.getNickName())
                .email(userRegisterDto.getEmail())
                .build();
        newUsers.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(newUsers);

        return new AuthResponse(newUsers.getNickName(), null);
    }

    public AuthResponse loginUser(UserLoginDto userLoginDto) {

        Users foundUser = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // matches(입력된 비밀번호, 암호화된 비밀번호)
        if(!passwordEncoder.matches(userLoginDto.getPassword(), foundUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new AuthResponse(foundUser.getNickName(), tokenProvider.createToken(foundUser));
    }

    public Users updateUser(UserUpdateDto userUpdateDto) {
        Users foundUser = userRepository.findByEmail(userUpdateDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return foundUser;
    }
}
