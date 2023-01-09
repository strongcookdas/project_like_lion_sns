package com.example.project_travel_sns.util.alarm;

import com.example.project_travel_sns.domain.entity.Alarm;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.AlarmRepository;

public class AlarmUtil {
    public static void saveAlarm(AlarmRepository alarmRepository, AlarmType alarmType, User fromUser, Post post) {
        Alarm alarm = Alarm.of(alarmType, fromUser, post);
        alarmRepository.save(alarm);
    }
}
