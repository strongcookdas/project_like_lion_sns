package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.alarm.AlarmResponse;
import com.example.project_travel_sns.domain.entity.Alarm;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.AlarmRepository;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.util.post.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public Page<AlarmResponse> getAlarms(Pageable pageable, String userName) {
        //유저체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //알람 가져오기
        Page<Alarm> alarmPage = alarmRepository.findByUser(pageable, findUser);
        //알람 dto 변환 후 리턴
        Page<AlarmResponse> alarmResponses = AlarmResponse.listOf(alarmPage);
        return alarmResponses;
    }
}
