package com.example.project_travel_sns.domain.dto.like;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LikeResponse {
    private String result;

    public static LikeResponse of(String result) {
        return LikeResponse.builder()
                .result(result)
                .build();
    }
}
