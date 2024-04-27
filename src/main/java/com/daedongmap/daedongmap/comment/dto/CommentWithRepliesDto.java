package com.daedongmap.daedongmap.comment.dto;

import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentWithRepliesDto {

    private Long id;
    private UserBasicInfoDto user;
//    private Long reviewId;
    private String content;
    private Long parentId;
    private List<CommentDto> replies = new ArrayList<>();

    public CommentWithRepliesDto(CommentDto commentDto) {
        this.id = commentDto.getId();
        this.user = commentDto.getUser();
//        this.reviewId = commentDto.getReviewId();
        this.content = commentDto.getContent();
        this.parentId = commentDto.getParentId();
    }

    public void addReply(CommentDto commentDto) {
        replies.add(commentDto);
    }

}
