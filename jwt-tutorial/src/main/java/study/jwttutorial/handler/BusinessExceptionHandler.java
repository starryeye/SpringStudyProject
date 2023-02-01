package study.jwttutorial.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import study.jwttutorial.dto.ErrorDto;
import study.jwttutorial.exception.DuplicateMemberException;
import study.jwttutorial.exception.NotFoundMemberException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class BusinessExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateMemberException.class)
    public ErrorDto conflict(RuntimeException ex) {
        return new ErrorDto(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class, NotFoundMemberException.class})
    public ErrorDto forbidden(RuntimeException ex) {
        return new ErrorDto(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
