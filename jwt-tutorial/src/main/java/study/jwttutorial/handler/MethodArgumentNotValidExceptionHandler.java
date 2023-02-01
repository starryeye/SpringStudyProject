package study.jwttutorial.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import study.jwttutorial.dto.ErrorDto;

import java.util.List;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

//        for (FieldError fieldError : fieldErrors) {
//            fieldError.getField(); // 실패한 필드 이름
//            fieldError.getDefaultMessage(); // 바인딩 에러 디폴트 메세지
//            fieldError.getRejectedValue(); // 실패하게된 값
//        }

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), "@Valid 바인딩 에러");
        for (FieldError fieldError : fieldErrors) {
            errorDto.addFieldError(fieldError);
        }

        return errorDto;
    }
}
