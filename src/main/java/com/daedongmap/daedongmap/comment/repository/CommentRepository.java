package com.daedongmap.daedongmap.comment.repository;

import com.daedongmap.daedongmap.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByReviewId(Long reviewId);
    public void deleteById(Long id);
}
