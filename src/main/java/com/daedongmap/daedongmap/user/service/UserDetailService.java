package com.daedongmap.daedongmap.user.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserRepository userRepository;

    public Users loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
