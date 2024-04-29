package com.daedongmap.daedongmap.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
