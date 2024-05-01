package com.daedongmap.daedongmap.user.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthUserInfoDto {

    private String email;
    private String nickName;
    private String phoneNumber;

    @Builder
    public OAuthUserInfoDto(String email, String nickName, String phoneNumber) {
        this.email = email;
        this.nickName= nickName;
        this.phoneNumber = phoneNumber;
    }
}
