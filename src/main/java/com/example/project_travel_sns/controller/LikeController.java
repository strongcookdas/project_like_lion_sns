package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.like.LikeResponse;
import com.example.project_travel_sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Response> like(Authentication authentication, @PathVariable Long postId) {
        String userName = authentication.getName();
        LikeResponse likeResponse = likeService.like(userName, postId);
        return ResponseEntity.ok().body(Response.of("SUCCESS", likeResponse));
    }

    @GetMapping
    public ResponseEntity<Response> likeCount(@PathVariable Long postId) {
        Long likeCount = likeService.likeCount(postId);
        return ResponseEntity.ok().body(Response.of("SUCCESS", likeCount));
    }
}
