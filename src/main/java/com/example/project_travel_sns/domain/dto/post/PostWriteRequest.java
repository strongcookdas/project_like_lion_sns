package com.example.project_travel_sns.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostWriteRequest {
    private String title;
    private String body;
}
