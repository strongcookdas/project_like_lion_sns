package com.example.project_travel_sns.domain.dto.join;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserJoinResponse {
    private Long userId;
    private String userName;

    public static UserJoinResponse of(Long userId, String userName) {
        return UserJoinResponse.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
