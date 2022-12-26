package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
