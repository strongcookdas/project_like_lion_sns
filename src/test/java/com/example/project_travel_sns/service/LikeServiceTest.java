package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.entity.Like;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.AlarmRepository;
import com.example.project_travel_sns.repository.LikeRepository;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LikeServiceTest {
    LikeService likeService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    LikeRepository likeRepository = mock(LikeRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);


    User user = User.builder()
            .userId(1L)
            .userName("홍길동")
            .password("0000")
            .build();
    Like like = Like.builder()
            .id(1L)
            .build();
    List<Like> likes = new ArrayList<>();
    Post post = Post.builder()
            .id(1L)
            .title("제목")
            .body("내용입니다.")
            .user(user)
            .likes(likes)
            .build();


    @BeforeEach
    void setUp() {
        likeService = new LikeService(likeRepository, userRepository, postRepository, alarmRepository);
    }

    //좋아요 누르기 성공
    @Test
    @DisplayName("좋아요 누르기 성공")
    void like_SUCCESS() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.save(like))
                .thenReturn(like);

        assertDoesNotThrow(() -> likeService.like(user.getUserName(), post.getId()));
    }

    //좋아요 누르기 실패(1) : 유저가 없는 경우
    @Test
    @DisplayName("좋아요 누르기 실패(1) : 유저가 없는 경우")
    void like_FAIL_user() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.save(like))
                .thenReturn(like);

        AppException exception = assertThrows(AppException.class, () -> likeService.like(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    //좋아요 누르기 실패(2) : 포스트가 없는 경우
    @Test
    @DisplayName("좋아요 누르기 실패(2) : 포스트가 없는 경우")
    void like_FAIL_post() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(likeRepository.save(like))
                .thenReturn(like);

        AppException exception = assertThrows(AppException.class, () -> likeService.like(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    //좋아요 취소 성공
    @Test
    @DisplayName("좋아요 취소 성공")
    void like_cancell_SUCCESS() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.findByPostAndUser(any(), any()))
                .thenReturn(Optional.of(like));

        assertDoesNotThrow(() -> likeService.like(user.getUserName(), post.getId()));
    }

    @Test
    @DisplayName("좋아요 카운트 리턴 성공")
    void like_count_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> likeService.likeCount(post.getId()));
    }

    @Test
    @DisplayName("좋아요 카운트 리턴 실패 : 포스트가 없는 경우")
    void like_count_FAIL_post() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> likeService.likeCount(post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }
}