package com.daedongmap.daedongmap.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindIdDto {

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;
}
