package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("좋아요 누르기 성공")
    @WithMockUser
    void like_SUCCESS() throws Exception {
        when(likeService.like(any(), any()))
                .thenReturn("좋아요를 눌렀습니다.");

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("좋아요 누르기 실패(1) : 로그인을 하지 않은 경우")
    @WithAnonymousUser
    void like_FAIL_login() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("좋아요 누르기 실패(2) : 포스트가 없는 경우")
    @WithMockUser
    void like_FAIL_post() throws Exception {
        when(likeService.like(any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    @WithMockUser
    void like_cancel_SUCCESS() throws Exception {
        when(likeService.like(any(), any()))
                .thenReturn("좋아요를 취소했습니다.");

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("좋아요 개수 리턴 성공")
    @WithMockUser
    void like_count_SUCCESS() throws Exception {
        when(likeService.likeCount(any()))
                .thenReturn(3L);

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("좋아요 카운트 리턴 실패 : 포스트가 없는 경우")
    @WithMockUser
    void like_count_FAIL_post() throws Exception {
        when(likeService.likeCount(any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.errorCode").exists());
    }
}