package ru.yandex.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.yandex.constant.Constant.DATE_TIME_FORMATTER;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onControllerArgumentNotValidException(HttpMessageNotReadableException e) {
        log.info("Получен статус 409 Bad Request {}", e.getMessage(), e);
        return new Violation(
                "409 Bad Request",
                Objects.requireNonNull(e.getMessage()),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onControllerArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("Получен статус 409 Bad Request {}", e.getMessage(), e);
        return new Violation(
                "409 Bad Request",
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ExceptionHandler(DataNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Violation onServiceArgumentNotValidException(RuntimeException e) {
        log.info("Получен статус 404 Not Found {}", e.getMessage(), e);
        return new Violation(
                "404 Not Found",
                Objects.requireNonNull(e.getMessage()),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onServiceArgumentNotValidException(IllegalArgumentException e) {
        log.info("Получен статус 400 Conflict {}", e.getMessage(), e);
        return new Violation(
                "400 Conflict",
                Objects.requireNonNull(e.getMessage()),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }
}
