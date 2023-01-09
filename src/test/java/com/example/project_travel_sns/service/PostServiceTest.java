package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
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

class PostServiceTest {

    PostService postService;
    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    ServiceAppInfo serviceAppInfo = new ServiceAppInfo();
    User user = serviceAppInfo.getUser();
    User user2 = serviceAppInfo.getUser2();
    Post post = serviceAppInfo.getPost();
    Post modifyPost = serviceAppInfo.getModifyPost();

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void post_write_SUCCESS() {

        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.save(any()))
                .thenReturn(post);

        assertDoesNotThrow(() -> postService.write(user.getUserName(), post.getTitle(), post.getBody()));
        PostResponse postResponse = postService.write(user.getUserName(), post.getTitle(), post.getBody());
        assertEquals(postResponse.getMessage(), "포스트 등록이 완료되었습니다.");
        assertEquals(postResponse.getPostId(), post.getId());

    }

    @Test
    @DisplayName("포스트 등록 실패 : 유저가 없는 경우")
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
        PostResponse postResponse = postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody());
        assertEquals(postResponse.getMessage(), "포스트 수정 완료");
    }

    @Test
    @DisplayName("포스트 수정 실패(1) : 포스트가 존재하지 않는 경우")
    void post_modify_FAILED_not_found_post() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패(2) : 포스트 작성자와 유저가 다른 경우")
    void post_modify_FAILED_different() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user2.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패(3) : 유저가 존재하지 않는 경우")
    void post_modify_FAILED_not_found_userName() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> postService.modify(user.getUserName(), post.getId(), modifyPost.getTitle(), modifyPost.getBody()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 성공")
    void post_delete_SUCCESS() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> postService.delete(user.getUserName(), post.getId()));
        PostResponse postResponse = postService.delete(user.getUserName(), post.getId());
        assertEquals(postResponse.getMessage(), "포스트 삭제 완료");
    }

    @Test
    @DisplayName("포스트 삭제 실패(1) : 포스트가 존재하지 않는 경우")
    void post_delete_FAILED_not_found_post() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 실패(2) : 포스트 작성자와 유저가 다른 경우")
    void post_delete_FAILED_different() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user2.getUserName(), post.getId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 실패(3) : 유저가 존재하지 않는 경우")
    void post_delete_FAILED_not_found_userName() {
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }
}