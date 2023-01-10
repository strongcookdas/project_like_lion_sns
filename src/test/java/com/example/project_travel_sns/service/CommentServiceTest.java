package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.comment.CommentDeleteResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentModifyResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.AlarmRepository;
import com.example.project_travel_sns.repository.CommentRepository;
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

class CommentServiceTest {

    CommentService commentService;
    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    CommentRepository commentRepository = mock(CommentRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);
    ServiceAppInfo serviceAppInfo = new ServiceAppInfo();
    User user = serviceAppInfo.getUser();
    User user2 = serviceAppInfo.getUser2();
    Post post = serviceAppInfo.getPost();
    Comment comment = serviceAppInfo.getComment();

    @BeforeEach
    void setUp() {
        commentService = new CommentService(userRepository, postRepository, commentRepository, alarmRepository);
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
        CommentResponse commentResponse = commentService.write(user.getUserName(), post.getId(), comment.getComment());
        assertEquals(commentResponse.getId(), 1L);
        assertEquals(commentResponse.getComment(), "테스트입니다.");
    }

    @Test
    @DisplayName("댓글 작성 실패(1) : 유저가 없는 경우")
    void comment_write_FAIL_login() {
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
    @DisplayName("댓글 작성 실패(2) : 포스트가 없는 경우")
    void comment_write_FAIL_post() {
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
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        assertDoesNotThrow(() -> commentService.modify(user.getUserName(), post.getId(), comment.getId(), comment.getComment()));
        CommentModifyResponse commentModifyResponse = commentService.modify(user.getUserName(), post.getId(), comment.getId(), comment.getComment());
        assertEquals(commentModifyResponse.getId(), comment.getId());
        assertEquals(commentModifyResponse.getComment(), comment.getComment());
    }

    @Test
    @DisplayName("댓글 수정 실패(1) : 유저가 존재하지 않는 경우")
    void comment_modify_FAIL_user() {
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
    @DisplayName("댓글 수정 실패(2) : 포스트가 없는 경우")
    void comment_modify_FAIL_post() {
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
    @DisplayName("댓글 수정 실패(3) : 작성자 불일치인 경우")
    void comment_modify_FAIL_different() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.saveAndFlush(any()))
                .thenReturn(comment);

        AppException exception = assertThrows(AppException.class, () -> commentService.modify(user2.getUserName(), post.getId(), comment.getId(), comment.getComment()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void comment_delete_SUCCESS() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.delete(user.getUserName(), post.getId(), comment.getId()));
        CommentDeleteResponse commentDeleteResponse = commentService.delete(user.getUserName(), post.getId(), comment.getId());
        assertEquals(commentDeleteResponse.getMessage(), "댓글 삭제 완료");
        assertEquals(commentDeleteResponse.getId(), comment.getId());
    }

    @Test
    @DisplayName("댓글 삭제 실패(1) : 유저가 존재하지 않는 경우")
    void comment_delete_FAIL_user() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));

        AppException exception = assertThrows(AppException.class, () -> commentService.delete(user.getUserName(), post.getId(), comment.getId()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패(2) : 포스트가 존재하지 않는 경우")
    void comment_delete_FAIL_post() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));

        AppException exception = assertThrows(AppException.class, () -> commentService.delete(user.getUserName(), post.getId(), comment.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패(3) : 댓글이 존재하지 않는 경우")
    void comment_delete_FAIL_comment() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> commentService.delete(user.getUserName(), post.getId(), comment.getId()));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패(4) : 작성자가 불인치인 경우")
    void comment_delete_FAIL_different() {
        when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(user2));
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdAndId(any(), any()))
                .thenReturn(Optional.of(comment));

        AppException exception = assertThrows(AppException.class, () -> commentService.delete(user2.getUserName(), post.getId(), comment.getId()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}