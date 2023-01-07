package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
