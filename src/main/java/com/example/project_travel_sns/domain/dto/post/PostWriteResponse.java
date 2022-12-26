package com.example.project_travel_sns.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostWriteResponse {
    private String message;
    private Long postId;

    public static PostWriteResponse of(String message, Long postId){
        return PostWriteResponse.builder()
                .message(message)
                .postId(postId)
                .build();
    }
}
