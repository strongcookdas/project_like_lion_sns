package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.comment.CommentRequest;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @WithMockUser
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
}