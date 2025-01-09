package kr.alas.job.service.common.response;

import kr.alas.job.service.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse extends Response {

    private String error;
    private String message;
    private int errorCode;

    public ErrorResponse(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public static ErrorResponse of(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setError(ex.getSpecName());
        errorResponse.setErrorCode(ex.getSpecCode());
        errorResponse.setMessage(ex.getSpecMessage());

        return errorResponse;
    }
}
