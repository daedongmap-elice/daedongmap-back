package com.daedongmap.daedongmap.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateDto {

    private Long reviewId;
    private String content;
    private Long parentId;

}
