package com.luxixi.backend.common;
import com.luxixi.backend.contact.ContactMessageService.DuplicateMessageException;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("code","VALIDATION_ERROR","message","留言人和留言内容不能为空，且长度不合法"));}
 @ExceptionHandler(DuplicateMessageException.class) ResponseEntity<?> duplicate(){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("code","DUPLICATE_MESSAGE","message","请勿重复提交相同留言"));}
}
