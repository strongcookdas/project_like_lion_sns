package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse write(String userName, String title, String body) {
        //userName 찾기
        User findUser = userRepository.findByUserName(userName).orElseThrow(() -> {
            log.error("userName Not Found : {}", userName);
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });
        //포스트 저장
        Post savedPost = Post.of(title, body, findUser);
        savedPost = postRepository.save(savedPost);
        //포스트 DTO 반환
        PostResponse postResponse = PostResponse.of("포스트 등록이 완료되었습니다.", savedPost.getId());
        return postResponse;
    }

    public PostGetResponse getPost(Long id) {
        //해당 id 포스트 찾기
        Post findPost = postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
        //포스트 응답 DTO 변환 후 리턴
        PostGetResponse postGetResponse = PostGetResponse.of(findPost);
        return postGetResponse;
    }

    public PostResponse modify(String userName, Long id, String title, String body) {
        //포스트 체크
        Post modifyPost = postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
        //유저 체크
        User user = userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        });
        //포스트 유저와 유처 비교
        if (!modifyPost.getUser().getUserName().equals(user.getUserName())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
        //포스트 수정
        modifyPost.modify(title,body);
        postRepository.saveAndFlush(modifyPost);
        //포스트 응답 DTO 변환 후 반환
        PostResponse postResponse = PostResponse.of("포스트 수정 완료", modifyPost.getId());
        return postResponse;
    }

    public PostResponse delete(String userName, Long id) {
        //포스트 체크
        Post deletePost = postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
        //유저 체크
        User user = userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        });
        //포스트 유저와 유처 비교
        if (!deletePost.getUser().getUserName().equals(user.getUserName())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
        //포스트 삭제 후 DTO 리턴
        postRepository.deleteById(deletePost.getId());
        PostResponse postResponse = PostResponse.of("포스트 삭제 완료", deletePost.getId());
        return postResponse;
    }

    public Page<PostGetResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostGetResponse> postGetResponses = PostGetResponse.listOf(posts);
        return postGetResponses;
    }
}
