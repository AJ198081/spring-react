package dev.aj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FailedToPersistEntityException extends RuntimeException {

    public FailedToPersistEntityException() {
        super();
    }

    public FailedToPersistEntityException(String message) {
        super(message);
    }

    public FailedToPersistEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
