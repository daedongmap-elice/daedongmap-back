package com.daedongmap.daedongmap.user.controller;


import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.AuthResponse;
import com.daedongmap.daedongmap.user.dto.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.UserUpdateDto;
import com.daedongmap.daedongmap.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {

        AuthResponse authResponse = userService.registerUser(userRegisterDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> doLogin(@RequestBody @Valid UserLoginDto userLoginDto) {

        AuthResponse authResponse = userService.loginUser(userLoginDto);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(authResponse);
    }

    @GetMapping("/accountId")
    public String findUserId(@RequestBody String phoneNumber) {

        return "";
    }

    // 질문할 것 - id를 @PathVariable 로 받아오는 것이 적절한가
    @PutMapping("/user/{userId}")
    public ResponseEntity<Users> updateUser(@RequestBody UserUpdateDto userUpdateDto) {

        Users user = userService.updateUser(userUpdateDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

}
