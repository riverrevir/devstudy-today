package today.devstudy.controller;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import today.devstudy.dto.exception.ExceptionResponse;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> notFoundEntityException(final IllegalArgumentException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validException(){
        return new ResponseEntity<>(new ExceptionResponse("요청의 유효성을 만족하지 않습니다."),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> duplicateUniqueColumException(){
        return new ResponseEntity<>(new ExceptionResponse("중복 사용자가 존재합니다."),HttpStatus.BAD_REQUEST);
    }
}
