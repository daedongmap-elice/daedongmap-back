package com.daedongmap.daedongmap.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateDto {

    private String nickName;

    private String status;

    private String webSite;

    private String profileImageLink;
}
