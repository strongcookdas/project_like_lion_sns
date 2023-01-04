package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.CommentRepository;
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

class CommentServiceTest {

    CommentService commentService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    CommentRepository commentRepository = mock(CommentRepository.class);

    User user = User.builder()
            .userId(1L)
            .userName("홍길동")
            .password("0000")
            .build();

    User user2 = User.builder()
            .userId(2L)
            .userName("홍길동2")
            .password("0000")
            .build();

    Post post = Post.builder()
            .id(1L)
            .title("제목")
            .body("내용입니다.")
            .user(user)
            .build();

    Comment comment = Comment.builder()
            .id(1L)
            .comment("테스트입니다.")
            .user(user)
            .post(post)
            .build();

    @BeforeEach
    void setUp() {
        commentService = new CommentService(userRepository, postRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void comment_write_SUCCESS() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        assertDoesNotThrow(() -> commentService.write(user.getUserName(), post.getId(), comment.getComment()));
    }

    @Test
    @DisplayName("댓글 작성 실패1_로그인을 안한 경우")
    void comment_write_FALID_login() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.write(user.getUserName(), post.getId(), comment.getComment()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 작성 실패2_포스트가 없는 경우")
    void comment_write_FALID_post() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(commentRepository.save(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.write(user.getUserName(), post.getId(), comment.getComment()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void comment_modify_SUCCESS() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findById(any()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        assertDoesNotThrow(() -> commentService.modify(user.getUserName(), post.getId(), comment.getId(), comment.getComment()));
    }

    @Test
    @DisplayName("댓글 수정 실패1_유저가 존재하지 않는 경우")
    void comment_modify_FALID_user() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.modify(user.getUserName(), post.getId(), comment.getId(), comment.getComment()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 실패2_포스트가 없는 경우")
    void comment_modify_FALID_post() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.modify(user.getUserName(), post.getId(), comment.getId(), comment.getComment()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 실패3_작성자 불일치인 경우")
    void comment_modify_FALID_different() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findById(any()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.modify(user2.getUserName(), post.getId(), comment.getId(), comment.getComment()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}