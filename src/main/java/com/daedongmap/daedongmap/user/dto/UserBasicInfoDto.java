package com.daedongmap.daedongmap.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserBasicInfoDto {
    private Long id;
    private String nickName;
    private String email;
}
