package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.post.PostWriteResponse;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostWriteResponse write(String userName, String title, String body) {
        //userName 찾기
        User findUser = userRepository.findByUserName(userName).orElseThrow(() -> {
            log.error("userName Not Found : {}", userName);
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });
        //포스트 저장
        Post savedPost = Post.of(title, body, findUser);
        savedPost = postRepository.save(savedPost);
        //포스트 DTO 반환
        PostWriteResponse postWriteResponse = PostWriteResponse.of("포스트 등록이 완료되었습니다.", savedPost.getId());
        return postWriteResponse;
    }
}
