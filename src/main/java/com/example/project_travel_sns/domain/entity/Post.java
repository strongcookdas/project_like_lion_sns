package com.example.project_travel_sns.domain.entity;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Like> likes;

    public static Post of(String title, String body, User user) {
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
    }

    public void modify(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
