package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {

    private String nickName;
    private String email;
    private String status;
}
