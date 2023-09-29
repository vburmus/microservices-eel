package com.epam.esm.utils.exceptionhandler;

import com.epam.esm.utils.exceptionhandler.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<Problem> handleCertificateAlreadyExistException(NoSuchTagException e) {
        Problem problem = buildProblem(Status.NOT_FOUND, NO_SUCH_ITEM_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<Problem> handleTagAlreadyExistsException(TagAlreadyExistsException e) {
        Problem problem = buildProblem(Status.CONFLICT, ALREADY_EXIST_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<Problem> handleInvalidTagException(InvalidTagException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, INVALID_ITEM_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    private Problem buildProblem(Status status, String title, String detail) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(detail)
                .build();
    }
}