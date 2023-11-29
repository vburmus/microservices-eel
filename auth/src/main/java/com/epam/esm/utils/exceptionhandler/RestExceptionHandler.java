package com.epam.esm.utils.exceptionhandler;

import com.epam.esm.utils.exceptionhandler.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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

import static com.epam.esm.utils.AuthConstants.AN_INTERNAL_SERVER_ERROR_OCCURRED_WHILE_PROCESSING_THE_REQUEST;
import static com.epam.esm.utils.Constants.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : INVALID_USER_ERROR;
        Problem problem = buildProblem(Status.BAD_REQUEST, INVALID_USER_ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class,
            MalformedJwtException.class})
    public ResponseEntity<Problem> handleJwtExceptions(RuntimeException e) {
        Problem problem = buildProblem(Status.UNAUTHORIZED, AUTHENTICATION_EXCEPTION, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Problem> handleBadCredentials() {
        Problem problem = buildProblem(Status.UNAUTHORIZED, AUTHENTICATION_EXCEPTION, WRONG_EMAIL_OR_PASSWORD);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler({EmailNotFoundException.class, EmailAlreadyRegisteredException.class})
    public ResponseEntity<Problem> handleEmailNotFoundException(RuntimeException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, CREDENTIALS_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({CacheException.class, IncorrectTokenTypeException.class})
    public ResponseEntity<Problem> handleCacheError() {
        Problem problem = buildProblem(Status.INTERNAL_SERVER_ERROR, Status.INTERNAL_SERVER_ERROR.toString(),
                AN_INTERNAL_SERVER_ERROR_OCCURRED_WHILE_PROCESSING_THE_REQUEST);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler({InvalidTokenException.class, TokenRequiredException.class, AccountIsActiveException.class})
    public ResponseEntity<Problem> handleTokenExceptions(RuntimeException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, TOKEN_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({InvalidFileException.class, NullableFileException.class, ImageUploadException.class})
    public ResponseEntity<Problem> handleInvalidFileException(InvalidFileException e) {
        Problem problem = Problem.builder()
                .withStatus(Status.UNPROCESSABLE_ENTITY)
                .withTitle(FILE_UPLOAD_ERROR)
                .withDetail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem);
    }

    private Problem buildProblem(Status status, String title, String detail) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(detail)
                .build();
    }
}