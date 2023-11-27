package com.epam.esm.utils.exceptionhandler;

import com.epam.esm.utils.exceptionhandler.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import static com.epam.esm.utils.Constants.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(NoSuchObjectException.class)
    public ResponseEntity<Problem> handleNoSuchObjectException(NoSuchObjectException e) {
        Problem problem = buildProblem(Status.NOT_FOUND, NO_SUCH_ITEM_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ObjectAlreadyExists.class)
    public ResponseEntity<Problem> handleObjectAlreadyExistsException(ObjectAlreadyExists e) {
        Problem problem = buildProblem(Status.CONFLICT, ALREADY_EXIST_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Problem> handlePropertyReferenceException(PropertyReferenceException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, SORT_PARAMETER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<Problem> handleCertificateUpdateException(UpdateException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, UPDATE_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(PurchasePositionException.class)
    public ResponseEntity<Problem> handlePurchasePositionException(PurchasePositionException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, PURCHASE_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Problem> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Problem problem = buildProblem(Status.BAD_REQUEST, Status.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({JsonPatchException.class, JsonProcessingException.class})
    public ResponseEntity<Problem> handleJsonException(RuntimeException ex) {
        Problem problem = buildProblem(Status.INTERNAL_SERVER_ERROR, JSON_EXCEPTION, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : INVALID_ITEM_ERROR;
        Problem problem = buildProblem(Status.BAD_REQUEST, INVALID_ITEM_ERROR, errorMessage);
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