package dev.aj.exception;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(final Exception ex, final WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                                                .errorMessage("%s %s".formatted(request.getDescription(true), ex.getMessage()))
                                                .timestamp(ZonedDateTime.now())
                                                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
