package com.daedongmap.daedongmap.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
    private Long userId;
}
