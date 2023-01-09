package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.comment.CommentRequest;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.CommentService;
import com.example.project_travel_sns.util.ControllerAppInfo;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    ControllerAppInfo controllerAppInfo = new ControllerAppInfo();

    @Test
    @DisplayName("댓글 작성 성공")
    @WithMockUser
    void comment_write_SUCCESS() throws Exception {

        when(commentService.write(any(), any(), any()))
                .thenReturn(controllerAppInfo.getCommentResponse());

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists());
    }

    @Test
    @DisplayName("댓글 작성 실패(1) : 로그인을 하지 않은 경우")
    @WithAnonymousUser
    void comment_write_FAILD_login() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 작성 실패(2) : 포스트가 없는 경우")
    @WithMockUser
    void comment_write_FAILD_() throws Exception {

        when(commentService.write(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void comment_modify_SUCCESS() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenReturn(controllerAppInfo.getCommentModifyResponse());

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());
    }

    @Test
    @DisplayName("댓글 수정 실패(1) : 로그인을 하지 않은 경우")
    @WithAnonymousUser
    void comment_modify_FAILD_login() throws Exception {

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 수정 실패(2) : 포스트가 없는 경우")
    @WithMockUser
    void comment_modify_FAIL_post() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }

    @Test
    @DisplayName("댓글 수정 실패(3) : 작성자 불일치인 경우")
    @WithMockUser
    void comment_modify_FAIL_different() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }

    @Test
    @DisplayName("댓글 수정 실패(4) : DB 에러인 경우")
    @WithMockUser
    void comment_modify_FAIL_db() throws Exception {

        when(commentService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest("테스트입니다."))))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void comment_delete_SUCCESS() throws Exception {

        when(commentService.delete(any(), any(), any()))
                .thenReturn(controllerAppInfo.getCommentDeleteResponse());

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists());

    }

    @Test
    @DisplayName("댓글 삭제 실패(1) : 로그인을 하지 않은 경우")
    @WithAnonymousUser
    void comment_delete_FAILD_user() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
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
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
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
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
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