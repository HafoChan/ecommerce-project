package com.sohan.user_service.exception;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Locale;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized_exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "invalid_key", HttpStatus.BAD_REQUEST),

    FULL_NAME_NOT_BLANK(1002, "full_name_not_blank", HttpStatus.BAD_REQUEST),
    FULL_NAME_SIZE(1003, "full_name_size", HttpStatus.BAD_REQUEST),

    PHONE_EXISTS(1004, "phone_exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_NOT_BLANK(1005, "phone_number_not_blank", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_PATTERN(1006, "phone_number_pattern", HttpStatus.BAD_REQUEST),

    EMAIL_EXISTS(1007, "email_exists", HttpStatus.CONFLICT),
    EMAIL_NOT_BLANK(1008, "email_not_blank", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1009, "email_invalid", HttpStatus.BAD_REQUEST),

    USERNAME_EXISTS(1010, "username_exists", HttpStatus.CONFLICT),
    USERNAME_NOT_BLANK(1011, "username_not_blank", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE(1012, "username_size", HttpStatus.BAD_REQUEST),

    PASSWORD_NOT_BLANK(1013, "password_not_blank", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE(1014, "password_size", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTED(1015, "user_not_existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXIST(1016, "role_not_exist", HttpStatus.NOT_FOUND),
    ROLE_EXISTS(1017, "role_exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_EXIST(1018, "permission_not_exist", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTS(1019, "permission_exists", HttpStatus.CONFLICT),
    TOKEN_EXPIRED(1020, "token_expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1021, "token_invalid", HttpStatus.UNAUTHORIZED),

    UNAUTHENTICATED(1100, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1200, "unauthorized", HttpStatus.FORBIDDEN);

    ErrorCode(int code, String messageKey, HttpStatusCode statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String messageKey;
    private final HttpStatusCode statusCode;

    public String getMessageKey(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(this.messageKey, null, locale);
    }
}
