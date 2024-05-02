package com.daedongmap.daedongmap.user.dto.response;

import com.daedongmap.daedongmap.user.domain.Users;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {

    private String nickName;
    private String email;
    private String status;
    private String webSite;
    private String profileImage;

    @Builder
    public UserResponseDto(Users user) {
        this.nickName = user.getNickName();
        this.status = user.getStatus();
        this.email = user.getEmail();
        this.webSite = user.getWebSite();
        this.profileImage = user.getProfileImage();
    }
}
