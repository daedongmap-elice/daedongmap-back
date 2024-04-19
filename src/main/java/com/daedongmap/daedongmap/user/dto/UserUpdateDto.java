package com.daedongmap.daedongmap.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;

    private String status;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
