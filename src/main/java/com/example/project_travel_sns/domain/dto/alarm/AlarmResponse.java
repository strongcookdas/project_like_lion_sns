package com.example.project_travel_sns.domain.dto.alarm;

import com.example.project_travel_sns.domain.entity.Alarm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AlarmResponse {
    private Long id;
    private String alarmType;
    private Long fromUserId;
    private Long targetId;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> listOf(Page<Alarm> alarms) {
        Page<AlarmResponse> alarmResponses = alarms.map(m -> AlarmResponse.builder()
                .id(m.getId())
                .alarmType(m.getAlarmType())
                .fromUserId(m.getFromUser().getUserId())
                .targetId(m.getTargetPost().getId())
                .text(m.getText())
                .createdAt(m.getCreatedAt())
                .build());
        return alarmResponses;
    }
}
