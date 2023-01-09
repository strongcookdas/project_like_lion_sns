package com.example.project_travel_sns.service;

import com.example.project_travel_sns.domain.dto.join.UserJoinResponse;
import com.example.project_travel_sns.domain.dto.login.UserLoginResponse;
import com.example.project_travel_sns.domain.entity.User;
import com.example.project_travel_sns.exception.AppException;
import com.example.project_travel_sns.exception.ErrorCode;
import com.example.project_travel_sns.repository.UserRepository;
import com.example.project_travel_sns.configuration.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.key}")
    private String key;

    public UserJoinResponse join(String userName, String password) {
        //userName 중복확인
        userRepository.findByUserName(userName).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });
        //저장
        User savedUser = User.of(userName, encoder.encode(password));
        savedUser = userRepository.save(savedUser);
        //ResponseDTO
        return UserJoinResponse.of(savedUser.getUserId(), savedUser.getUserName());
    }

    public UserLoginResponse login(String userName, String password) {
        final long expireTimeMs = 1000 * 60 * 60L;
        //userName 체크
        User selectedUser = userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
        });
        //패스워드 체크
        if (!encoder.matches(password, selectedUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }
        //토큰 발행
        String token = JwtUtil.createToken(selectedUser.getUserName(), key, expireTimeMs);
        return UserLoginResponse.of(token);
    }
}
