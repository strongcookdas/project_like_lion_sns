package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.join.UserJoinRequest;
import com.example.project_travel_sns.domain.dto.join.UserJoinResponse;
import com.example.project_travel_sns.domain.dto.login.UserLoginRequest;
import com.example.project_travel_sns.domain.dto.login.UserLoginResponse;
import com.example.project_travel_sns.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    public ResponseEntity<Response> join(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest.getUserName(), userJoinRequest.getPassword());
        return ResponseEntity.ok().body(Response.of("SUCCESS", userJoinResponse));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<Response> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return ResponseEntity.ok().body(Response.of("SUCCESS",userLoginResponse));
    }
}
