package kr.alas.job.service.common.exception;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component("errorMessage")
public class ErrorMessageKr implements ErrorMessage {

    private final List<Pair<ErrorSpec, String>> errorMessages = Arrays.asList(
            Pair.of(ErrorSpec.ERROR_SPEC_AccessDeniedException, "접근이 거부되었습니다. - %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Auth_Forbidden, "%s 에 대한 접근권한이 없습니다."),

            Pair.of(ErrorSpec.ERROR_SPEC_OAuth_FeignException_Unauthorized, "인증 정보를 확인해주세요. (FeignClient)"),
            Pair.of(ErrorSpec.ERROR_SPEC_OAuth_FeignException_Bad_Request, "요청 정보가 잘못되었습니다. (FeignClient)"),
            Pair.of(ErrorSpec.ERROR_SPEC_Resource_Not_Found, "해당 리소스를 찾을 수 없습니다. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "파라메터 오류. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Illegal_State, "요청정보가 부적절합니다. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Illegal_Argument, "인자가 부적절합니다."),

            Pair.of(ErrorSpec.ERROR_SPEC_Internal_UnKnown, "API 내부 오류. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Internal_UnKnown_Message, "%s"),

            Pair.of(ErrorSpec.ERROR_SPEC_Login_Fail, "로그인에 실패하였습니다. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Duplicated, "%s 이 중복 입니다."),

            Pair.of(ErrorSpec.ERROR_SPEC_Need_InputData, "입력 정보가 필요합니다. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Employee_Data_Invalid, "사원 정보가 부적절합니다. %s"),

            Pair.of(ErrorSpec.ERROR_SPEC_Need_Approval_Data, "결재 정보가 없습니다. %s"),
            Pair.of(ErrorSpec.ERROR_SPEC_Approval_Data_Invalid, "결재 정보가 부적절합니다. %s"),

            Pair.of(ErrorSpec.ERROR_SPEC_IMAGE_UPLOAD_FAIL, "이미지 업로드가 실패했습니다. %s")
    );

    @Override
    public String get(ErrorSpec spec) {
        return errorMessages.stream()
                .filter(p -> spec.equals(p.getFirst()))
                .findAny()
                .map(Pair::getSecond)
                .orElse("");
    }
}
