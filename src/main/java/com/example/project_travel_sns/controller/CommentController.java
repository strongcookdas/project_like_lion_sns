package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.comment.CommentDeleteResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentModifyResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentRequest;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.service.CommentService;
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

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Api(tags = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @ApiOperation(value = "댓글 목록 조회")
    public ResponseEntity<Response> getComments(@ApiIgnore @PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long postId) {
        Page<CommentResponse> commentResponses = commentService.getComments(pageable, postId);
        return ResponseEntity.ok().body(Response.of("SUCCESS", commentResponses));
    }

    @PostMapping
    @ApiOperation(value = "댓글 작성")
    public ResponseEntity<Response> write(@ApiIgnore Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = commentService.write(userName, postId, commentRequest.getComment());
        return ResponseEntity.ok().body(Response.of("SUCCESS", commentResponse));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "댓글 수정")
    public ResponseEntity<Response> modify(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentModifyResponse commentModifyResponse = commentService.modify(userName, postId, id, commentRequest.getComment());
        return ResponseEntity.ok().body(Response.of("SUCCESS", commentModifyResponse));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "댓글 삭제")
    public ResponseEntity<Response> delete(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long id) {
        String userName = authentication.getName();
        CommentDeleteResponse commentDeleteResponse = commentService.delete(userName, postId, id);
        return ResponseEntity.ok().body(Response.of("SUCCESS", commentDeleteResponse));
    }
}
