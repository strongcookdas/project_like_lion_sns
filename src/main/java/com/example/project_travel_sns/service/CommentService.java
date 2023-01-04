package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.comment.CommentModifyResponse;
import com.example.project_travel_sns.domain.dto.comment.CommentResponse;
import com.example.project_travel_sns.domain.entity.Comment;
import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.repository.CommentRepository;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.util.post.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentResponse write(String userName, Long postId, String comment) {
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 엔티티 변환 후 저장
        Comment saveComment = Comment.of(comment, findUser, findPost);
        saveComment = commentRepository.save(saveComment);
        //댓글 DTO 리턴
        return CommentResponse.of(saveComment);
    }

    public CommentModifyResponse modify(String userName, Long postId, Long id, String comment) {
        //유저체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 체크
        Comment findComment = AppUtil.findComment(commentRepository, id);
        //작성자 체크
        AppUtil.compareUser(findComment.getUser().getUserName(), findUser.getUserName());
        //댓글 수정
        findComment.modify(comment);
        commentRepository.saveAndFlush(findComment);
        return CommentModifyResponse.of(findComment);
    }
}
