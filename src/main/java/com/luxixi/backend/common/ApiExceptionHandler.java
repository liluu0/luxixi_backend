package com.luxixi.backend.common;

import com.luxixi.backend.contact.ContactMessageService.DuplicateMessageException;
import com.luxixi.backend.contact.InvalidContactMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("code", "VALIDATION_ERROR", "message", "留言人和留言内容不能为空，且长度不合法"));
    }

    @ExceptionHandler(DuplicateMessageException.class)
    ResponseEntity<?> duplicate() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("code", "DUPLICATE_MESSAGE", "message", "请勿重复提交相同留言"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<?> malformedRequest(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("code", "INVALID_REQUEST", "message", "请求格式不正确"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<?> unsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of("code", "UNSUPPORTED_MEDIA_TYPE", "message", "仅支持 application/json 请求"));
    }

    @ExceptionHandler(InvalidContactMessageException.class)
    ResponseEntity<?> invalidContactMessage(InvalidContactMessageException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("code", "INVALID_MESSAGE", "message", e.getMessage()));
    }

}
