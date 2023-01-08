package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.Like;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
}
