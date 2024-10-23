package com.sohan.product_service.exception;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Locale;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1101, "Invalid message key", HttpStatus.BAD_REQUEST),

    TOKEN_EXPIRED(1102, "Token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1103, "Invalid token", HttpStatus.UNAUTHORIZED),

    CATEGORY_NOT_FOUND(1104, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1105, "Product not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1100, "Unauthenticated user", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1200, "You do not have permission", HttpStatus.FORBIDDEN);

    ErrorCode(int code, String messageKey, HttpStatusCode statusCode) {
        this.code = code;
        this.message = messageKey;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    public String getMessage(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(this.message, null, locale);
    }
}
