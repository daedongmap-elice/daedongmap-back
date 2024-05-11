package com.daedongmap.daedongmap.user.dto.response;

import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDto {

    private String nickName;
    private Users user;
//    private JwtTokenDto token;
    private List<Authority> roles = new ArrayList<>();

}
