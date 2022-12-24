package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.join.UserJoinResponse;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public UserJoinResponse join(String userName, String password) {
        //userName 중복확인
        userRepository.findByUserName(userName).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });
        //저장
        User savedUser = User.of(userName, encoder.encode(password));
        savedUser = userRepository.save(savedUser);
        //ResponseDTO
        UserJoinResponse userJoinResponse = UserJoinResponse.of(savedUser.getUserId(), savedUser.getUserName());
        return userJoinResponse;
    }
}
