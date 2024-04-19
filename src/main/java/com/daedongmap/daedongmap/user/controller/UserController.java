package com.daedongmap.daedongmap.user.controller;


import com.daedongmap.daedongmap.user.dto.AuthResponse;
import com.daedongmap.daedongmap.user.dto.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.UserRegisterDto;
import com.daedongmap.daedongmap.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
