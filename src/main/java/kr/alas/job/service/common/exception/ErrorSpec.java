package kr.alas.job.service.common.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorSpec {
    // AUTH
    ERROR_SPEC_AccessDeniedException(UNAUTHORIZED, Group.AUTH.code(1)),
    ERROR_SPEC_Auth_Forbidden(FORBIDDEN, Group.AUTH.code(2)),
    ERROR_SPEC_Auth_Otp(FORBIDDEN, Group.AUTH.code(3)),

    // REQUEST
    ERROR_SPEC_OAuth_FeignException_Unauthorized(UNAUTHORIZED, Group.REQUEST.code(1)),
    ERROR_SPEC_OAuth_FeignException_Bad_Request(BAD_REQUEST, Group.REQUEST.code(2)),
    ERROR_SPEC_Resource_Not_Found(NOT_FOUND, Group.REQUEST.code(3)),
    ERROR_SPEC_Invalid_Parameter(BAD_REQUEST, Group.REQUEST.code(4)),
    ERROR_SPEC_Illegal_State(BAD_REQUEST, Group.REQUEST.code(5)),
    ERROR_SPEC_Illegal_Argument(BAD_REQUEST, Group.REQUEST.code(6)),

    // INTERNAL
    ERROR_SPEC_Internal_UnKnown(INTERNAL_SERVER_ERROR, Group.INTERNAL.code(1)),
    ERROR_SPEC_Internal_UnKnown_Message(INTERNAL_SERVER_ERROR, Group.INTERNAL.code(2)),

    // ACCOUNT
    ERROR_SPEC_Login_Fail(NOT_FOUND, Group.ACCOUNT.code(1)),
    ERROR_SPEC_Duplicated(BAD_REQUEST, Group.ACCOUNT.code(2)),

    // EMPLOYEE
    ERROR_SPEC_Need_InputData(BAD_REQUEST, Group.EMPLOYEE.code(1)),
    ERROR_SPEC_Employee_Data_Invalid(BAD_REQUEST, Group.EMPLOYEE.code(2)),

    // APPROVAL
    ERROR_SPEC_Need_Approval_Data(BAD_REQUEST, Group.APPROVAL.code(1)),
    ERROR_SPEC_Approval_Data_Invalid(BAD_REQUEST, Group.APPROVAL.code(2)),

    // S3
    ERROR_SPEC_IMAGE_UPLOAD_FAIL(INTERNAL_SERVER_ERROR, Group.S3.code(1));


    private final HttpStatus httpStatus;
    private final int code;

    public enum Group {
        AUTH(0),
        REQUEST(1),
        INTERNAL(2),
        ACCOUNT(3),
        EMPLOYEE(4),
        APPROVAL(5),
        S3(6);

        private final int code;

        Group(int code) {
            this.code = code * 10000;
        }

        public int code(int value) {
            return code + value;
        }
    }
}
