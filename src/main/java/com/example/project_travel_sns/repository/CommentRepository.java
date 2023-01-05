package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
}
