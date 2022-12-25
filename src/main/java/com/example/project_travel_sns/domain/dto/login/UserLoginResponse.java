package com.example.project_travel_sns.domain.dto.login;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserLoginResponse {
    private String token;

    public static UserLoginResponse of(String token){
        return UserLoginResponse.builder()
                .token(token)
                .build();
    }
}
