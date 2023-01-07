package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.like.LikeResponse;
import com.example.project_travel_sns.domain.entity.Like;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.LikeRepository;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.util.post.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public LikeResponse like(String userName, Long postId) {
        //유저체크
        User findUser = AppUtil.findUser(userRepository,userName);
        //포스트체크
        Post findPost = AppUtil.findPost(postRepository,postId);
        //엔티티 생성 후 저장
        Like like = Like.of(findUser,findPost);
        likeRepository.save(like);
        //likeResponse 리턴
        return LikeResponse.of("좋아요를 눌렀습니다.");
    }
}