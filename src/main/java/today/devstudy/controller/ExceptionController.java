package today.devstudy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import today.devstudy.dto.exception.ExceptionResponse;

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

}
