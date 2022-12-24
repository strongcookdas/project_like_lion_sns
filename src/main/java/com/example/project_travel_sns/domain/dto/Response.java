package com.example.project_travel_sns.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Response<T> {

    private String resultCode;
    private T result;

    public static <T> Response of(String resultCode, T result) {
        return Response.builder()
                .resultCode(resultCode)
                .result(result)
                .build();
    }

}
