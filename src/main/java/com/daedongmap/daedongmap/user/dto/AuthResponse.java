package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponse {

    private String nickName;
    private String token;
}
