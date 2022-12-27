package com.example.project_travel_sns.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostRequest {
    private String title;
    private String body;
}
