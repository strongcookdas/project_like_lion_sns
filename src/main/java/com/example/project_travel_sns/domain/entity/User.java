package com.example.project_travel_sns.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> myPosts;

    public static User of(String userName, String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .build();
    }
}
