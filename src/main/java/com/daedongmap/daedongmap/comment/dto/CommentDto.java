package com.daedongmap.daedongmap.comment.dto;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private Long id;
    private UserBasicInfoDto user;
    private String content;
    private Long parentId = null;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto (Comment comment) {
        this.id = comment.getId();
        this.user = new UserBasicInfoDto(comment.getUser());
        this.content = comment.getContent();
        this.parentId = comment.getParentId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

}
