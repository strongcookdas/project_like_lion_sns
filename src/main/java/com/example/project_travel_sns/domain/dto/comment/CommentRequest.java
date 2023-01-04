package com.example.project_travel_sns.domain.dto.comment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentRequest {
    private String comment;
}
