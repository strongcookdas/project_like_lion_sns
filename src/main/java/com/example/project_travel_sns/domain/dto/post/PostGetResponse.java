package com.example.project_travel_sns.domain.dto.post;

import com.example.project_travel_sns.domain.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PostGetResponse {
    private Long id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static PostGetResponse of(Post post) {
        return PostGetResponse.builder()
                .id(post.getId())
                .userName(post.getUser().getUserName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getModifiedAt())
                .build();
    }

    public static Page<PostGetResponse> listOf(Page<Post> posts) {
        Page<PostGetResponse> postGetResponses = posts.map(m -> PostGetResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .body(m.getBody())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt())
                .lastModifiedAt(m.getModifiedAt())
                .build());
        return postGetResponses;
    }
}
