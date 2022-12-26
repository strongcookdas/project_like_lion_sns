package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostServiceTest {

    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void post_write_SUCCESS() {
        Post post = mock(Post.class);
        User user = mock(User.class);

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.save(any()))
                .thenReturn(post);

        assertDoesNotThrow(() -> postService.write("아무개", "테스트", "테스트입니다."));
    }

    @Test
    @DisplayName("포스트 등록 실패_로그인을 하지않은 경우")
    void post_write_FAILED() {
        Post post = mock(Post.class);

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.save(any()))
                .thenReturn(post);

        AppException exception = assertThrows(AppException.class, () -> postService.write("아무개", "테스트", "테스트입니다."));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }
}