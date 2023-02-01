package study.jwttutorial.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorDto {
    private final int status;
    private final String message;

    private List<FieldError> fieldErrors = new ArrayList<>();


    public void addFieldError(FieldError fieldError) {
        FieldError error = new FieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        fieldErrors.add(error);
    }
}
