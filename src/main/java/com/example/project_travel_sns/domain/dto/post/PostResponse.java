package com.example.project_travel_sns.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostResponse {
    private String message;
    private Long postId;

    public static PostResponse of(String message, Long postId){
        return PostResponse.builder()
                .message(message)
                .postId(postId)
                .build();
    }
}
