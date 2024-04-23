package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.*;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
                .status(userRegisterDto.getStatus())
                .email(userRegisterDto.getEmail())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .build();
        newUsers.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        newUsers.setRoles(Collections.singletonList(Authority.builder().role("ROLE_USER").build()));
        userRepository.save(newUsers);

        return new AuthResponseDto(newUsers.getNickName(), null, null);
    }

    public AuthResponseDto loginUser(UserLoginDto userLoginDto) {

        Users foundUser = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // matches(입력된 비밀번호, 암호화된 비밀번호)
        if(!passwordEncoder.matches(userLoginDto.getPassword(), foundUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new AuthResponseDto(
                foundUser.getNickName(),
                tokenProvider.createToken(foundUser.getEmail(), foundUser.getRoles()),
                foundUser.getRoles()
        );
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

        // @Transactional 어노테이션을 붙이면 JPA에서 트랜잭션이 끝나는 시점에서 변화가 생긴 엔티티를 모두 자동으로 반영
        // 조회 시 스냅샷을 만들고 종료 시 스냅샷과 차이가 있다면 DB에 이를 반영한다.
//        userRepository.save(foundUser);

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
