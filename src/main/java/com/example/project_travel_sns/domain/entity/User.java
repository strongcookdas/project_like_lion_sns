package com.example.project_travel_sns.domain.entity;

import lombok.*;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "user")
    private List<Post> myPosts;

    public static User of(String userName, String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .build();
    }
}
