package com.daedongmap.daedongmap.user.controller;


import com.daedongmap.daedongmap.user.domain.CustomUserDetails;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserFindIdDto;
import com.daedongmap.daedongmap.user.dto.request.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import com.daedongmap.daedongmap.user.dto.response.AuthResponseDto;
import com.daedongmap.daedongmap.user.dto.response.UserResponseDto;
import com.daedongmap.daedongmap.user.service.TokenService;
import com.daedongmap.daedongmap.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    @Operation(summary = "사용자 등록", description = "userRegisterDto를 통해 받은 정보로 사용자 정보를 DB에 저장")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {

        AuthResponseDto authResponseDto = userService.registerUser(userRegisterDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authResponseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인", description = "userLoginDto를 통해 받은 정보로 사용자 로그인, jwt 반환")
    public ResponseEntity<AuthResponseDto> doLogin(@RequestBody @Valid UserLoginDto userLoginDto) {

        AuthResponseDto authResponseDto = userService.loginUser(userLoginDto);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(authResponseDto);
    }

    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃", description = "DB 로그아웃하는 사용자의 리프레시 토큰 삭제")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

        String refreshToken = request.getHeader("Authorization");

        Long userId = tokenService.validate(refreshToken);

        String deleteMessage = tokenService.deleteByUserId(userId);

        log.info(deleteMessage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deleteMessage);
    }

    @PostMapping("/user/accountId")
    @Operation(summary = "사용자 아이디 찾기", description = "회원가입 시 입력한 휴대폰 번호를 통해 아이디 찾기")
    public ResponseEntity<String> retrieveUserId(@RequestBody @Valid UserFindIdDto userFindIdDto) {

        String userId = userService.retrieveUserId(userFindIdDto.getPhoneNumber());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userId);
    }

    // 다른 사용자의 정보 확인하기 - 다른 사람의 마이페이지 조회
    @GetMapping("/user/{userId}")
    @Operation(summary = "다른 사용자의 정보 조회", description = "토큰의 userId를 통해 다른 사용자에 대한 정보를 출력")
    public ResponseEntity<UserResponseDto> findOtherUserById(@PathVariable(name = "userId") Long userId) {

        UserResponseDto userResponseDto = userService.findUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    // 현재 로그인한 사용자의 정보 조회
    @GetMapping("/user")
    @Operation(summary = "사용자 정보 조회", description = "토큰의 userId를 통해 현재 사용자에 대한 정보를 출력")
    public ResponseEntity<UserResponseDto> findCurrentById(@AuthenticationPrincipal CustomUserDetails tokenUser) {

        UserResponseDto userResponseDto = userService.findUserById(tokenUser.getUser().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    // Put or patch?
    @PutMapping("/user")
    @Operation(summary = "사용자 정보 업데이트", description = "UserUpdateDto를 통해 업데이트할 정보를 전달")
    public ResponseEntity<Users> updateUser(@RequestBody UserUpdateDto userUpdateDto,
                                            @AuthenticationPrincipal CustomUserDetails tokenUser) {

        Users user = userService.updateUser(tokenUser.getUser().getId(), userUpdateDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @DeleteMapping("/user")
    @Operation(summary = "사용자 삭제", description = "이메일(PK)을 통해 사용자 조회 확인 후 삭제, 삭제된 사용자의 닉네임 반환")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails tokenUser) {

        String deletedUser = userService.deleteUser(tokenUser.getUser().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deletedUser);

    }
}
