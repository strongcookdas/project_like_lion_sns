package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);
}
