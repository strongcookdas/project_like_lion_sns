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
import com.example.project_travel_sns.util.ServiceAppInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    ServiceAppInfo serviceAppInfo = new ServiceAppInfo();
    User user = serviceAppInfo.getUser();
    Post post = serviceAppInfo.getPost();
    Like like = serviceAppInfo.getLike();

    @BeforeEach
    void setUp() {
        likeService = new LikeService(likeRepository, userRepository, postRepository, alarmRepository);
    }

    @Test
    @DisplayName("좋아요 누르기 성공")
    void like_SUCCESS() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.save(any()))
                .thenReturn(like);

        String likeResponse = likeService.like(user.getUserName(), post.getId());
        assertEquals(likeResponse, "좋아요를 눌렀습니다.");
    }

    @Test
    @DisplayName("좋아요 누르기 실패(1) : 유저가 없는 경우")
    void like_FAIL_user() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.save(any()))
                .thenReturn(like);

        AppException exception = assertThrows(AppException.class, () -> likeService.like(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 누르기 실패(2) : 포스트가 없는 경우")
    void like_FAIL_post() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(likeRepository.save(any()))
                .thenReturn(like);

        AppException exception = assertThrows(AppException.class, () -> likeService.like(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void like_cancel_SUCCESS() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(likeRepository.findByPostAndUser(any(), any()))
                .thenReturn(Optional.of(like));

        assertDoesNotThrow(() -> likeService.like(user.getUserName(), post.getId()));
        String likeResponse = likeService.like(user.getUserName(), post.getId());
        assertEquals(likeResponse, "좋아요를 취소했습니다.");
    }

    @Test
    @DisplayName("좋아요 카운트 리턴 성공")
    void like_count_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> likeService.likeCount(post.getId()));
        Long count = likeService.likeCount(post.getId());
        assertEquals(count, 0L);
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