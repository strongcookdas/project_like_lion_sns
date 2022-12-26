package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostWriteRequest;
import com.example.project_travel_sns.domain.dto.post.PostWriteResponse;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.PostService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;
    @Autowired
    ObjectMapper objectMapper;

    PostGetResponse postGetResponse = PostGetResponse.builder()
            .id(1l)
            .userName("홍길동")
            .title("제목")
            .body("내용입니다.")
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now()).build();
    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void post_write_SUCCESS() throws Exception {
        String title = "테스트";
        String body = "테스트입니다.";

        String message = "포스트 등록 완료";
        Long id = 1l;

        when(postService.write(any(), any(), any()))
                .thenReturn(new PostWriteResponse(message, id));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("포스트 작성 실패_인증")
    @WithMockUser
    void post_write_FAILED_authentication() throws Exception {
        String title = "테스트";
        String body = "테스트입니다.";

        when(postService.write(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 작성 실패_토큰 만료")
    @WithMockUser
    void post_write_FAILED() throws Exception {
        String title = "테스트";
        String body = "테스트입니다.";

        when(postService.write(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void post_get_detail_SUCCESS() throws Exception {
        String title = "테스트";
        String body = "테스트입니다.";

        when(postService.getPost(any()))
                .thenReturn(postGetResponse);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists());
    }
}