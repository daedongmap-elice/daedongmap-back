package com.daedongmap.daedongmap.user.controller;


import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserFindIdDto;
import com.daedongmap.daedongmap.user.dto.request.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import com.daedongmap.daedongmap.user.dto.response.AuthResponseDto;
import com.daedongmap.daedongmap.user.dto.response.UserResponseDto;
import com.daedongmap.daedongmap.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

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
    
    // post 메소드 사용
    @PostMapping("/user/accountId")
    @Operation(summary = "사용자 아이디 찾기", description = "회원가입 시 입력한 휴대폰 번호를 통해 아이디 찾기")
    public ResponseEntity<String> retrieveUserId(@RequestBody @Valid UserFindIdDto userFindIdDto) {

        String userId = userService.retrieveUserId(userFindIdDto.getPhoneNumber());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userId);
    }

    // 질문할 것 - id를 @PathVariable 로 받아오는 것이 적절한가
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 정보 조회", description = "userId를 통해 사용자에 대한 정보를 출력")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable(name = "userId") Long userId) {

        UserResponseDto userResponseDto = userService.findUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PutMapping("/user/{userId}")
    @Operation(summary = "사용자 정보 업데이트", description = "UserUpdateDto를 통해 업데이트할 정보를 전달")
    public ResponseEntity<Users> updateUser(@PathVariable(name = "userId") Long userId,
                                            @RequestBody UserUpdateDto userUpdateDto) {

        Users user = userService.updateUser(userId, userUpdateDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "사용자 삭제", description = "이메일(PK)을 통해 사용자 조회 확인 후 삭제, 삭제된 사용자의 닉네임 반환")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long userId) {

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteUser(userId));

    }
}
