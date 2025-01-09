package kr.alas.job.service.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class Response {

    private HttpStatus status;
    private int statusCode;

    public Response() {
        status = HttpStatus.OK;
        statusCode = HttpStatus.OK.value();
    }

    public Response(HttpStatus status) {
        this.status = status;
        this.statusCode = status.value();
    }

//    public static Response of() {
//        return new Response();
//    }

}
