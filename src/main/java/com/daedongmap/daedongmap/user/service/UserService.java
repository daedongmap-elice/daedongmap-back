package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.*;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public AuthResponseDto registerUser(UserRegisterDto userRegisterDto) {

        userRepository.findByEmail(userRegisterDto.getEmail()).ifPresent(e -> {
            throw new CustomException(ErrorCode.EMAIL_IN_USE);
        });

        Users newUsers = Users.builder()
                .nickName(userRegisterDto.getNickName())
                .email(userRegisterDto.getEmail())
                .build();
        newUsers.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(newUsers);

        return new AuthResponseDto(newUsers.getNickName(), null);
    }

    public AuthResponseDto loginUser(UserLoginDto userLoginDto) {

        Users foundUser = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // matches(입력된 비밀번호, 암호화된 비밀번호)
        if(!passwordEncoder.matches(userLoginDto.getPassword(), foundUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new AuthResponseDto(foundUser.getNickName(), tokenProvider.createToken(foundUser));
    }

    public String retrieveUserId(String phoneNumber) {
        Users foundUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return foundUser.getEmail();
    }

    public UserResponseDto findUserById(Long userId) {
        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDto(foundUser);
    }

    @Transactional
    public Users updateUser(Long userId, UserUpdateDto userUpdateDto) {
        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        foundUser.updateUser(userUpdateDto);
        userRepository.save(foundUser);

        return foundUser;
    }

    @Transactional
    public String deleteUser(Long userId) {

        Users deletedUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.deleteById(userId);

        return deletedUser.getNickName();
    }

}
