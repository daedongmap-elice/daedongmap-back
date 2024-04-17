package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.User;
import com.daedongmap.daedongmap.user.dto.AuthResponse;
import com.daedongmap.daedongmap.user.dto.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.UserRegisterDto;
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

    @Transactional
    public AuthResponse registerUser(UserRegisterDto userRegisterDto) {

        Optional<User> foundUser = userRepository.findByEmail(userRegisterDto.getEmail());

        if(foundUser.isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_IN_USE);
        }

        User newUser = User.builder()
                .nickName(userRegisterDto.getNickName())
                .status(userRegisterDto.getStatus())
                .email(userRegisterDto.getEmail())
                .build();
        newUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(newUser);

        return new AuthResponse(newUser.getNickName());
    }

    public AuthResponse loginUser(UserLoginDto userLoginDto) {
        Optional<User> foundUser = userRepository.findByEmail(userLoginDto.getEmail());

        if(foundUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = foundUser.get();
        
        // matches(입력된 비밀번호, 암호화된 비밀번호)
        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new AuthResponse(user.getNickName());
    }
}
