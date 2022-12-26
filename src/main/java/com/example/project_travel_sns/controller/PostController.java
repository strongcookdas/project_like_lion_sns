package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostWriteRequest;
import com.example.project_travel_sns.domain.dto.post.PostWriteResponse;
import com.example.project_travel_sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPost(@PathVariable Long id){
        PostGetResponse postGetResponse = postService.getPost(id);
        return ResponseEntity.ok().body(Response.of("SUCCESS",postGetResponse));
    }

    @PostMapping("")
    public ResponseEntity<Response> writePost(@RequestBody PostWriteRequest postWriteRequest, Authentication authentication){
        String userName = authentication.getName();
        PostWriteResponse postWriteResponse = postService.write(userName,postWriteRequest.getTitle(),postWriteRequest.getBody());
        return ResponseEntity.ok().body(Response.of("SUCCESS",postWriteResponse));
    }
}
