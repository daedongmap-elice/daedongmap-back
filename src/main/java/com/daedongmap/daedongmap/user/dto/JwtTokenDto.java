package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
}