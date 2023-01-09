package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.post.PostGetResponse;
import com.example.project_travel_sns.domain.dto.post.PostResponse;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.util.post.AppUtil;
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
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 저장
        Post savedPost = Post.of(title, body, findUser);
        savedPost = postRepository.save(savedPost);
        //포스트 DTO 반환
        return PostResponse.of("포스트 등록이 완료되었습니다.", savedPost.getId());
    }

    public PostGetResponse getPost(Long id) {
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, id);
        //포스트 응답 DTO 변환 후 리턴
        return PostGetResponse.of(findPost);
    }

    public PostResponse modify(String userName, Long id, String title, String body) {
        //포스트 체크
        Post modifyPost = AppUtil.findPost(postRepository, id);
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 유저와 유처 비교
        AppUtil.compareUser(modifyPost.getUser().getUserName(), findUser.getUserName());
        //포스트 수정 후 저장
        modifyPost.modify(title, body);
        postRepository.saveAndFlush(modifyPost);
        //포스트 응답 DTO 변환 후 반환
        return PostResponse.of("포스트 수정 완료", modifyPost.getId());
    }

    public PostResponse delete(String userName, Long id) {
        //포스트 체크
        Post deletePost = AppUtil.findPost(postRepository, id);
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 유저와 유처 비교
        AppUtil.compareUser(deletePost.getUser().getUserName(), findUser.getUserName());
        //포스트 삭제 후 DTO 리턴
        postRepository.delete(deletePost);
        return PostResponse.of("포스트 삭제 완료", deletePost.getId());
    }

    public Page<PostGetResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return PostGetResponse.listOf(posts);
    }

    public Page<PostGetResponse> getMyPosts(Pageable pageable, String userName) {
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 불러오기
        Page<Post> myPosts = postRepository.findByUser(pageable, findUser);
        //PostGetResponseDto로 변환 후 리턴
        return PostGetResponse.listOf(myPosts);
    }
}
