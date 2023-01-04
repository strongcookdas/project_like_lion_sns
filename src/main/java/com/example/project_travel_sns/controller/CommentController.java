package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.comment.CommentRequest;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Response> write(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = commentService.write(userName, postId, commentRequest.getComment());
        return ResponseEntity.ok().body(Response.of("SUCCESS", commentResponse));
    }
}
