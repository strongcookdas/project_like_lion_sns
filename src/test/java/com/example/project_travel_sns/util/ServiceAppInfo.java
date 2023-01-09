package com.example.project_travel_sns.util;

import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.domain.entity.Like;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServiceAppInfo {
    private final User user = User.builder()
            .userId(1L)
            .userName("홍길동")
            .password("0000")
            .build();
    private final User user2 = User.builder()
            .userId(2L)
            .userName("홍길동2")
            .password("0000")
            .build();
    private final Like like = Like.builder()
            .id(1L)
            .build();
    private final List<Like> likes = new ArrayList<>();
    private final Post post = Post.builder()
            .id(1L)
            .title("제목")
            .body("내용입니다.")
            .user(user)
            .likes(likes)
            .build();

    private final Post modifyPost = Post.builder()
            .id(1L)
            .title("제목2")
            .body("내용입니다.2")
            .user(user)
            .build();

    private final Comment comment = Comment.builder()
            .id(1L)
            .comment("테스트입니다.")
            .user(user)
            .post(post)
            .build();
}
