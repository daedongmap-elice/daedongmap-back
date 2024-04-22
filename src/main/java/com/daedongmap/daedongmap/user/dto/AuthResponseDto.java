package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDto {

    private String nickName;
    private JwtTokenDto token;
}
