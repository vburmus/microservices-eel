package com.epam.esm.utils.exceptionhandler;

import com.epam.esm.utils.exceptionhandler.exceptions.FileUploadException;
import com.epam.esm.utils.exceptionhandler.exceptions.NullableFileException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import static com.epam.esm.utils.Constants.FILE_UPLOAD_ERROR;
import static com.epam.esm.utils.Constants.NULLABLE_FILE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(NullableFileException.class)
    public ResponseEntity<Problem> handleNullableFileException(NullableFileException e) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(NULLABLE_FILE)
                .withDetail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Problem> handleFileUploadException(FileUploadException e) {
        Problem problem = Problem.builder()
                .withStatus(Status.valueOf(e.getHttpStatus()))
                .withTitle(FILE_UPLOAD_ERROR)
                .withDetail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.valueOf(e.getHttpStatus())).body(problem);
    }
}