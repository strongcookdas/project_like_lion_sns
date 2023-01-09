package com.example.project_travel_sns.repository;

import com.example.project_travel_sns.domain.entity.Alarm;
import com.example.project_travel_sns.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByUser(Pageable pageable, User user);
}
