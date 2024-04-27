package com.daedongmap.daedongmap.user.dto.response;

import com.daedongmap.daedongmap.user.domain.Authority;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDto {

    private String nickName;
//    private JwtTokenDto token;
    private String token;
    private List<Authority> roles = new ArrayList<>();

}
