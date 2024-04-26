package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtParseDto {

    private String email;
    private String userId;
    private String role;

    @Builder
    public  JwtParseDto(String email, String userId, String role) {
        this.email = email;
        this.userId = userId;
        this.role = role;
    }
}
