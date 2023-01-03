package com.example.project_travel_sns.domain.dto.post;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostRequest {
    @NotEmpty(message = "제목을 입력해 주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해 주세요.")
    private String body;
}
