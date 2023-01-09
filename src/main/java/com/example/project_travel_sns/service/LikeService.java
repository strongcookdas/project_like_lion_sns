package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.entity.Like;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.LikeRepository;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.util.post.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public String like(String userName, Long postId) {
        //유저체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //좋아요 체크
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(findPost, findUser);
        if (optionalLike.isPresent()) {
            Like findLike = optionalLike.get();
            likeRepository.delete(findLike);
            return "좋아요를 취소했습니다.";
        }
        //엔티티 생성 후 저장
        Like like = Like.of(findUser, findPost);
        likeRepository.save(like);
        //likeResponse 리턴
        return "좋아요를 눌렀습니다.";
    }

    public Long likeCount(Long postId) {
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //좋아요 갯수 리턴
        return findPost.getLikes().stream().count();
    }
}
