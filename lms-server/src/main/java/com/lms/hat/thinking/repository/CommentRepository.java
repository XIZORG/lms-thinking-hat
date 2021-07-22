package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
