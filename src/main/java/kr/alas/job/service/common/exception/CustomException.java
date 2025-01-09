package kr.alas.job.service.common.exception;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private static ErrorMessage errorMessage;

    private HttpStatus httpStatus;
    private int specCode;
    private String specName;
    private String specMessage;

    @Autowired
    public void setErrorMessage(ErrorMessage errorMessage) {
        CustomException.errorMessage = errorMessage;
    }

    public CustomException(Exception ex) {
        super(ex);
    }

    public static CustomException of(Exception exception, ErrorSpec spec, String... args) {
        CustomException ex = new CustomException(exception);
        ex.httpStatus = spec.getHttpStatus();
        ex.specName = spec.name();
        ex.specCode = spec.getCode();
        ex.specMessage = CustomException.getMessage(spec, args);
        return ex;
    }

    public static CustomException of(ErrorSpec spec, String... args) {
        CustomException ex = new CustomException();
        ex.httpStatus = spec.getHttpStatus();
        ex.specName = spec.name();
        ex.specCode = spec.getCode();
        ex.specMessage = CustomException.getMessage(spec, args);
        return ex;
    }

    public static String getMessage(ErrorSpec spec, String... args) {
        return Arrays.stream(args)
                    .reduce(errorMessage.get(spec), (m, a) -> m.replaceFirst("%s", a))
                    .replaceAll("%s", "");
    }
}

