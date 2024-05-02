package com.daedongmap.daedongmap.user.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthUserInfoDto {

    private String email;
    private String nickName;
    private String phoneNumber;
    private String profileImage;

    @Builder
    public OAuthUserInfoDto(String email, String nickName, String phoneNumber, String profileImage) {
        this.email = email;
        this.nickName= nickName;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }
}
