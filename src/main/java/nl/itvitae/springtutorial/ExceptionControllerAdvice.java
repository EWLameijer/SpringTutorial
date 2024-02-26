package nl.itvitae.springtutorial;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> inconsistentIdExceptionHandler(BadRequestException exception) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
