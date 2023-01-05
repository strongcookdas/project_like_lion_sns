package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.comment.CommentDeleteResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentModifyResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentRequest;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper;

    CommentResponse commentResponse = CommentResponse.builder()
            .id(1L)
            .comment("테스트입니다.")
            .userName("홍길동")
            .postId(1L)
            .createdAt(LocalDateTime.now())
            .build();

    CommentModifyResponse commentModifyResponse = CommentModifyResponse.builder()
            .id(1L)
            .comment("테스트입니다.")
            .userName("홍길동")
            .postId(1L)
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("댓글 작성 성공")
    @WithMockUser
    void comment_write_SUCCESS() throws Exception {

        when(commentService.write(any(), any(), any()))
                .thenReturn(commentResponse);

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 작성 실패1_로그인을 하지 않은 경우")
    @WithAnonymousUser
    void comment_write_FAILD_login() throws Exception {
        when(commentService.write(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 작성 실패2_포스트가 없는 경우")
    @WithMockUser
    void comment_write_FAILD_() throws Exception {
        when(commentService.write(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void comment_modify_SUCCESS() throws Exception {

        when(commentService.modify("홍길동", 1L, 1L, "테스트입니다."))
                .thenReturn(commentModifyResponse);

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정 실패1_로그인을 하지 않은 경우")
    @WithAnonymousUser
    void comment_modify_FAILD_login() throws Exception {
        when(commentService.modify("홍길동", 1L, 1L, "테스트입니다."))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 수정 실패2_작성자 불일치인 경우")
    @WithMockUser
    void comment_modify_FAIL_different() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 수정 실패3_DB 에러인 경우")
    @WithMockUser
    void comment_modify_FAIL_db() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void comment_delete_SUCCESS() throws Exception {

        when(commentService.delete("홍길동", 1L, 1L))
                .thenReturn(new CommentDeleteResponse("댓글 삭제 왼료", 1L));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제 실패1_유저가 없는 경우")
    @WithMockUser
    void comment_delete_FAILD_user() throws Exception {
        when(commentService.delete(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("댓글 삭제 실패1_포스트가 없는 경우")
    @WithMockUser
    void comment_delete_FAILD_post() throws Exception {
        when(commentService.delete(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("댓글 삭제 실패3_작성자 불일치인 경우")
    @WithMockUser
    void comment_delete_FAILD_different() throws Exception {

        when(commentService.delete(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 삭제 실패4_DB에러인 경우")
    @WithMockUser
    void comment_delete_FAILD_db() throws Exception {

        when(commentService.delete(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    @WithMockUser
    void comment_get_list_SUCCESS() throws Exception {
        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(commentService).getComments(pageableArgumentCaptor.capture(), any());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "createdAt"), pageRequest.getSort());
    }
}