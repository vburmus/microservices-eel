package com.epam.esm.utils.exceptionhandler;

import com.epam.esm.utils.exceptionhandler.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import static com.epam.esm.utils.Constants.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RestApiClientException.class)
    public ResponseEntity<Problem> handleRestApiClientException(RestApiClientException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, API_CALL_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(RestApiServerException.class)
    public ResponseEntity<Problem> handleRestApiServerException(RestApiServerException e) {
        Problem problem = buildProblem(Status.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : INVALID_USER_ERROR;
        Problem problem = buildProblem(Status.BAD_REQUEST, INVALID_USER_ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class})
    public ResponseEntity<Problem> handleExpiredJwtException(Exception e) {
        Problem problem = buildProblem(Status.UNAUTHORIZED, AUTHENTICATION_EXCEPTION, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Problem> handleEmailNotFoundException(EmailNotFoundException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, CREDENTIALS_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Problem> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, CREDENTIALS_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Problem> handleBadCredentialsException(BadCredentialsException ex) {
        Problem problem = buildProblem(Status.UNAUTHORIZED, ex.getMessage(), WRONG_EMAIL_OR_PASSWORD);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }
    @ExceptionHandler(CacheException.class)
    public ResponseEntity<Problem> handleCacheError(CacheException e) {
        Problem problem = buildProblem(Status.UNAUTHORIZED, e.getMessage(), WRONG_EMAIL_OR_PASSWORD);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }
    private Problem buildProblem(Status status, String title, String detail) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(detail)
                .build();
    }
}