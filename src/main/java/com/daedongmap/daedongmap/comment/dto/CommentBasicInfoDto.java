package com.daedongmap.daedongmap.comment.dto;

import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentBasicInfoDto {

    private UserBasicInfoDto user;
    private Long reviewId;
    private String content;

}
