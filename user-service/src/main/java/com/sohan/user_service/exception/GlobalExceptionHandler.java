package com.sohan.user_service.exception;

import com.sohan.user_service.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String getLocalizedMessage(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }

    // Quét tất cả các exception chưa được xử lý
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingAllException(Exception exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .success(false)
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(getLocalizedMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessageKey()))
                .build();
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(apiResponse);
    }

    // Cấu hình App exception cho ứng dụng
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .success(false)
                .message(getLocalizedMessage(exception.getErrorCode().getMessageKey()))
                .code(exception.getErrorCode().getCode())
                .build();

        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(apiResponse);
    }

    // Bắt sự kiện Unauthorized
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(
                    ApiResponse.builder()
                            .code(errorCode.getCode())
                            .success(false)
                            .message(getLocalizedMessage(errorCode.getMessageKey()))
                .build()
        );
    }

    // Bắt các Exception cấu hình bằng message trong validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.INVALID_KEY;
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .success(false)
                .message(getLocalizedMessage(errorCode.getMessageKey()))
                .build();
        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }
}
