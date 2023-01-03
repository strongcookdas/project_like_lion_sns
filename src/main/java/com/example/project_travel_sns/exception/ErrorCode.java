package com.example.project_travel_sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "UserName이 중복됩니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 UserName입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 다릅니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    INVALID_VALUE(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.");

    private HttpStatus httpStatus;
    private String message;
}
