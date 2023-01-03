package com.example.project_travel_sns.domain.dto.login;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserLoginRequest {
    @NotEmpty(message = "id를 입력해 주세요.")
    private String userName;
    @NotEmpty(message = "비밀번호를 입력해 주세요.")
    private String password;
}
