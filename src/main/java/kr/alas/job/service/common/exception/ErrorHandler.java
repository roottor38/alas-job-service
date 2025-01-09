package kr.alas.job.service.common.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.ServletException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import kr.alas.job.service.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * 4. exception 발생시 전역으로 처리할 exception handler 작성
 * &#064;RestControllerAdvice,  @ExceptionHandler 어노테이션을 이용하여 exception 발생시 적절한 에러 응답을 생성해서 리턴한다.
 * &#064;RestControllerAdvice  : 모든 rest 컨트롤러에서 발생하는 exception을 처리한다.
 * &#064;ExceptionHandler(xxException.class)  : 발생한 xxException에 대해서 처리하는 메소드를 작성한다.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(HttpServletRequest request, Exception exception) {
        print(exception);
        return ErrorResponse.of(CustomException.of(ErrorSpec.ERROR_SPEC_Internal_UnKnown, exception.getMessage()));
    }

    @ExceptionHandler(value = {CustomException.class})
    public ErrorResponse handleMsgSvcExceptions(HttpServletRequest request, CustomException exception) {
        print(exception);
        return ErrorResponse.of(exception);
    }

    //요청 데이터 이외의 부분 - 서블렛, 서버 등?
    @ExceptionHandler(value = {
        PropertyAccessException.class,
        ValidationException.class,
        HttpMessageConversionException.class,
        ServletException.class,
        BindException.class,
        MissingRequestHeaderException.class
    })
    public ErrorResponse handleRequestExceptions(HttpServletRequest request, Exception exception) {
        print(exception);
        if (exception.getCause() instanceof CustomException) {
            return ErrorResponse.of((CustomException) exception.getCause());
        }
        return ErrorResponse.of(CustomException.of(ErrorSpec.ERROR_SPEC_Internal_UnKnown, exception.getMessage()));
    }

    //프론트 요청 데이터(파라미터)가 원인일 때
    @ExceptionHandler(value = {
        DataAccessException.class,
        MismatchedInputException.class,
        UnexpectedTypeException.class,
        InvalidParameterException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class
    })
    public ErrorResponse invalidParameterExceptionHandler(HttpServletRequest request, Exception exception) {
        ErrorSpec spec;
        String message = exception.getMessage();
        if (exception.getCause() instanceof IllegalStateException) {
            spec = ErrorSpec.ERROR_SPEC_Illegal_State;
        } else if (exception.getCause() instanceof IllegalArgumentException) {
            spec = ErrorSpec.ERROR_SPEC_Illegal_Argument;
        } else {
            spec = ErrorSpec.ERROR_SPEC_Invalid_Parameter;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException)exception;
            message = ex.getBindingResult().getFieldErrors().stream()
                    .map(f -> String.format("%s", f.getDefaultMessage()))
                    .collect(Collectors.joining(", "));
        }
        exception = CustomException.of(spec, message);
        print(exception);
        return ErrorResponse.of((CustomException) exception);
    }

    private void print(Exception ex) {
        log.error("\n");
        log.error("Message => {}", ex.getMessage());
        log.error("Trace => {}", Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }
}
