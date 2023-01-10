package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
@Api(tags = "Like API")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    @ApiOperation(value = "좋아요 누르기")
    public ResponseEntity<Response> like(@ApiIgnore Authentication authentication, @PathVariable Long postId) {
        String userName = authentication.getName();
        String likeResponse = likeService.like(userName, postId);
        return ResponseEntity.ok().body(Response.of("SUCCESS", likeResponse));
    }

    @GetMapping
    @ApiOperation(value = "좋아요 개수 리턴")
    public ResponseEntity<Response> likeCount(@PathVariable Long postId) {
        Long likeCount = likeService.likeCount(postId);
        return ResponseEntity.ok().body(Response.of("SUCCESS", likeCount));
    }
}
