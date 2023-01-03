package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostRequest;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPost(@PathVariable Long id) {
        PostGetResponse postGetResponse = postService.getPost(id);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postGetResponse));
    }

    @GetMapping
    public ResponseEntity<Response<Page<PostGetResponse>>> getPosts(@PageableDefault(size = 20)@SortDefault(sort = "createdAt",direction = Sort.Direction.DESC)Pageable pageable){
        Page<PostGetResponse> postGetResponses = postService.getPosts(pageable);
        return ResponseEntity.ok().body(Response.of("SUCCESS",postGetResponses));
    }

    @PostMapping("")
    public ResponseEntity<Response> write(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.write(userName, postRequest.getTitle(), postRequest.getBody());
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> modify(@Valid @RequestBody PostRequest postRequest, @PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.modify(userName, id, postRequest.getTitle(), postRequest.getBody());
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName, id);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }
}
