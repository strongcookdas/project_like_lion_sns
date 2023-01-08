package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.like.LikeResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    LikeResponse likeResponse = LikeResponse.builder()
            .result("좋아요를 눌렀습니다.").build();

    // 좋아요 성공
    @Test
    @DisplayName("좋아요 누르기 성공")
    @WithMockUser
    void like_SUCCESS() throws Exception {
        when(likeService.like(any(), any()))
                .thenReturn(likeResponse);

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 좋아요 실패(1) : 유저가 없는 경우
    @Test
    @DisplayName("좋아요 누르기 실패(1) : 유저가 없는 경우")
    @WithMockUser
    void like_FAIL_user() throws Exception {
        when(likeService.like(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // 좋아요 실패(2) : 포스트가 없는 경우
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
                .andExpect(status().isNotFound());
    }

    //좋아요 취소 성공
    @Test
    @DisplayName("좋아요 취소 성공")
    @WithMockUser
    void like_cancel_SUCCESS() throws Exception {
        when(likeService.like(any(), any()))
                .thenReturn(new LikeResponse("좋아요를 취소했습니다."));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}