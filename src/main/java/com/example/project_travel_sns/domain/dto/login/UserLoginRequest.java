package com.example.project_travel_sns.domain.dto.login;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserLoginRequest {
    private String userName;
    private String password;
}
