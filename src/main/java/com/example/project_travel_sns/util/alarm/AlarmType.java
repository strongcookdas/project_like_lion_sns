package com.example.project_travel_sns.util.alarm;

import lombok.*;

@AllArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT_ON_POST("새로운 댓글이 달렸습니다."),
    NEW_LIKE_ON_POST("좋아요가 눌렸습니다.");

    private final String message;
}
