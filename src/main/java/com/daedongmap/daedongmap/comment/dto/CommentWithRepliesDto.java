package com.daedongmap.daedongmap.comment.dto;

import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentWithRepliesDto {

    private Long id;
    private UserBasicInfoDto user;
    private String content;
    private Long parentId = null;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> replies = new ArrayList<>();

    public CommentWithRepliesDto(CommentDto commentDto) {
        this.id = commentDto.getId();
        this.user = commentDto.getUser();
        this.content = commentDto.getContent();
        this.parentId = commentDto.getParentId();
        this.createdAt = commentDto.getCreatedAt();
        this.updatedAt = commentDto.getUpdatedAt();
    }

    public void addReply(CommentDto commentDto) {
        replies.add(commentDto);
    }

}
