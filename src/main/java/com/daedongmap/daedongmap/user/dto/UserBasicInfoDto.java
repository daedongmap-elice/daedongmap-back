package com.daedongmap.daedongmap.user.dto;

import com.daedongmap.daedongmap.user.domain.Users;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserBasicInfoDto {
    private Long id;
    private String nickName;
    private String email;
    private String profileImagePath;

    public UserBasicInfoDto(Users user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.profileImagePath = user.getProfileImage();
    }

}
