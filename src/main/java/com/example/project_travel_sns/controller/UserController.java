package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.join.UserJoinRequest;
import com.example.project_travel_sns.domain.dto.join.UserJoinResponse;
import com.example.project_travel_sns.domain.dto.login.UserLoginRequest;
import com.example.project_travel_sns.domain.dto.login.UserLoginResponse;
import com.example.project_travel_sns.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Response> join(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest.getUserName(), userJoinRequest.getPassword());
        return ResponseEntity.ok().body(Response.of("SUCCESS", userJoinResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return ResponseEntity.ok().body(Response.of("SUCCESS",userLoginResponse));
    }
}
