package com.daedongmap.daedongmap.comment.dto;

import lombok.Getter;

@Getter
public class CommentCreateDto {

    private Long userId;
    private Long reviewId;
    private String content;
    private Long parentId;

}
