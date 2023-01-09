package com.example.project_travel_sns.util;

import com.example.project_travel_sns.domain.dto.comment.CommentDeleteResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentModifyResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.domain.dto.join.UserJoinResponse;
import com.example.project_travel_sns.domain.dto.login.UserLoginResponse;
import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ControllerAppInfo {
    //회원가입 Response
    private final UserJoinResponse userJoinResponse = UserJoinResponse.builder()
            .userId(1L)
            .userName("홍길동")
            .build();
    //로그인 Response
    private final UserLoginResponse userLoginResponse = UserLoginResponse.builder()
            .token("토큰")
            .build();
    //포스트 상세 Response
    private final PostGetResponse postGetResponse = PostGetResponse.builder()
            .id(1L)
            .title("제목입니다.")
            .body("내용입니다.")
            .userName("홍길동")
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .build();
    //포스트 Response
    private final PostResponse postResponse = PostResponse.builder()
            .message("요청 처리되었습니다.")
            .postId(1L)
            .build();
    //댓글 Response
    private final CommentResponse commentResponse = CommentResponse.builder()
            .id(1L)
            .comment("댓글입니다.")
            .userName("홍길동")
            .postId(1L)
            .createdAt(LocalDateTime.now())
            .build();
    //댓글 수정 Response
    private final CommentModifyResponse commentModifyResponse = CommentModifyResponse.builder()
            .id(1L)
            .comment("댓글 수정했습니다.")
            .userName("홍길동")
            .postId(1L)
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .build();
    //댓글 삭제 Response
    private final CommentDeleteResponse commentDeleteResponse = CommentDeleteResponse.builder()
            .message("댓글이 삭제되었습니다.")
            .id(1L)
            .build();
}
