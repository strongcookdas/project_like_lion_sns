package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.comment.CommentDeleteResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Comment findComment = AppUtil.findComment(commentRepository, postId, id);
        //작성자 체크
        AppUtil.compareUser(findComment.getUser().getUserName(), findUser.getUserName());
        //댓글 수정
        findComment.modify(comment);
        commentRepository.saveAndFlush(findComment);
        return CommentModifyResponse.of(findComment);
    }

    public CommentDeleteResponse delete(String userName, Long postId, Long id) {
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, userName);
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 체크
        Comment findComment = AppUtil.findComment(commentRepository, postId, id);
        //작성자 비교
        AppUtil.compareUser(findComment.getUser().getUserName(), userName);
        //삭제
        commentRepository.delete(findComment);
        //DTO 리턴
        return CommentDeleteResponse.of("댓글 삭제 완료", findComment.getId());
    }

    public Page<CommentResponse> getComments(Pageable pageable, Long postId) {
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 찾기
        Page<Comment> comments = commentRepository.findByPost(pageable, findPost);
        return CommentResponse.listOf(comments);
    }
}
