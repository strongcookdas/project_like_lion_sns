package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostRequest;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = "Post API")
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    @ApiOperation(value = "포스트 상세 조회")
    public ResponseEntity<Response> getPost(@PathVariable Long id) {
        PostGetResponse postGetResponse = postService.getPost(id);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postGetResponse));
    }

    @GetMapping
    @ApiOperation(value = "포스트 목록 조회")
    public ResponseEntity<Response> getPosts(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostGetResponse> postGetResponses = postService.getPosts(pageable);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postGetResponses));
    }

    @GetMapping("/my")
    @ApiOperation(value = "마이피드")
    public ResponseEntity<Response> getMyPosts(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        Page<PostGetResponse> postGetResponses = postService.getMyPosts(pageable, userName);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postGetResponses));
    }

    @PostMapping("")
    @ApiOperation(value = "포스트 작성")
    public ResponseEntity<Response> write(@Valid @RequestBody PostRequest postRequest, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.write(userName, postRequest.getTitle(), postRequest.getBody());
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "포스트 수정")
    public ResponseEntity<Response> modify(@Valid @RequestBody PostRequest postRequest, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.modify(userName, id, postRequest.getTitle(), postRequest.getBody());
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "포스트 삭제")
    public ResponseEntity<Response> delete(@PathVariable Long id,@ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName, id);
        return ResponseEntity.ok().body(Response.of("SUCCESS", postResponse));
    }

}
