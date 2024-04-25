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

    @Builder
    public UserResponseDto(Users user) {
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.status = user.getStatus();
    }
}
