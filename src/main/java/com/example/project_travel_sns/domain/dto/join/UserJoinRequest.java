package com.example.project_travel_sns.domain.dto.join;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserJoinRequest {
    private String userName;
    private String password;
}
