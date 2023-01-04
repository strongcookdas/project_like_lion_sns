package com.example.project_travel_sns.util.post;

import com.example.project_travel_sns.domain.entity.Post;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.PostRepository;
import com.example.project_travel_sns.repository.UserRepository;

public class PostUtil {
    //유저 체크
    public static User findUser(UserRepository userRepository, String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        });
    }

    //포스트 체크
    public static Post findPost(PostRepository postRepository, Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }

    //포스트 유저와 유저 비교
    public static void comparePostUser(String postUserName, String userName) {
        if (!postUserName.equals(userName)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }
}
