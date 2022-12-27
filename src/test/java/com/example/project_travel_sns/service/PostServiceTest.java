package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
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

    User user = User.builder()
            .userId(1l)
            .userName("홍길동")
            .password("0000")
            .build();
    User user2 = User.builder()
            .userId(2l)
            .userName("홍길동2")
            .password("0000")
            .build();
    Post post = Post.builder()
            .id(1l)
            .title("제목")
            .body("내용입니다.")
            .user(user)
            .build();
    Post modifyPost = Post.builder()
            .id(1l)
            .title("제목2")
            .body("내용입니다.2")
            .user(user)
            .build();

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

    @Test
    @DisplayName("포스트 상세조회 성공")
    void post_get_detail_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        PostGetResponse postGetResponse = postService.getPost(post.getId());
        assertEquals(postGetResponse.getUserName(), post.getUser().getUserName());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    void post_modify_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

       assertDoesNotThrow(() -> postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
    }

    @Test
    @DisplayName("포스트 수정 실패1_포스트가 존재하지 않는 경우")
    void post_modify_FAILED_not_found_post() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패2_포스트 작성자와 유저가 다른 경우")
    void post_modify_FAILED_different() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user2.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패3_유저가 존재하지 않는 경우")
    void post_modify_FAILED_not_found_userName() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
    @Test
    @DisplayName("포스트 삭제 성공")
    void post_delete_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> postService.delete(user.getUserName(), post.getId()));
    }

    @Test
    @DisplayName("포스트 삭제 실패1_포스트가 존재하지 않는 경우")
    void post_delete_FAILED_not_found_post() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패2_포스트 작성자와 유저가 다른 경우")
    void post_delete_FAILED_different() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user2.getUserName(), post.getId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패3_유저가 존재하지 않는 경우")
    void post_delete_FAILED_not_found_userName() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}