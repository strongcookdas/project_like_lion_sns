package com.example.project_travel_sns.domain.entity;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public static Post of(String title, String body, User user) {
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
    }

    public PostGetResponse toResponse(){
        return PostGetResponse.builder()
                .id(this.id)
                .userName(this.user.getUserName())
                .title(this.title)
                .body(this.body)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getModifiedAt())
                .build();
    }
}
