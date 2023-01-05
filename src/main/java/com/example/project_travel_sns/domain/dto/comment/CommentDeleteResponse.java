package com.example.project_travel_sns.domain.dto.comment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentDeleteResponse {
    private String message;
    private Long id;

    public static CommentDeleteResponse of(String message, Long id) {
        return CommentDeleteResponse.builder()
                .message(message)
                .id(id)
                .build();
    }
}
