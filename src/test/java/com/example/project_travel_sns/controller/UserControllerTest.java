package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.join.UserJoinRequest;
import com.example.project_travel_sns.domain.dto.login.UserLoginRequest;
import com.example.project_travel_sns.domain.dto.login.UserLoginResponse;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.service.UserService;
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

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_SUCCESS() throws Exception {

        String userName = "홍길동";
        String password = "0000";

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패_userName이 중복인 경우")
    @WithMockUser
    void join_FAILED() throws Exception {

        String userName = "홍길동";
        String password = "0000";

        when(userService.join(any(),any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME,ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_SUCCESS() throws Exception {

        String userName = "홍길동";
        String password = "0000";

        when(userService.login(any(),any()))
                .thenReturn(new UserLoginResponse("token"));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패_userName이 존재하지 않은 경우")
    @WithMockUser
    void login_FAILED_userName() throws Exception {

        String userName = "홍길동";
        String password = "0000";

        when(userService.login(any(),any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패_패스워드가 다른 경우")
    @WithMockUser
    void login_FAILED_password() throws Exception {

        String userName = "홍길동";
        String password = "0000";

        when(userService.login(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}