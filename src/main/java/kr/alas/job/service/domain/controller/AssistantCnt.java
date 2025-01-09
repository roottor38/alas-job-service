package kr.alas.job.service.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.stream.Collectors;
import kr.alas.job.service.common.response.CommonResponse;
import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.model.AssistantModel;
import kr.alas.job.service.domain.model.AssistantModel.ResPage;
import kr.alas.job.service.domain.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/assistant")
@RestController
@RequiredArgsConstructor
public class AssistantCnt {

    private final AssistantService assistantService;

    @Operation(summary = "Assistant 목록 조회", description = "사용가능 Assistant 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "api 호출 성공",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "api 호출 실패")
    })
    @GetMapping("/page")
    public CommonResponse<List<AssistantModel.ResPage>> getPage() {
        Page<Assistant> result = assistantService.getPage();
        return CommonResponse.ofList(result.get()
            .map(ResPage::of)
            .collect(Collectors.toList()), result.getTotalElements());
    }

    @Operation(summary = "Assistant 목록 조회", description = "사용가능 Assistant 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "api 호출 성공",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "api 호출 실패")
    })
    @PostMapping("/run")
    public CommonResponse<AssistantModel.Res> runAssistant(@RequestBody AssistantModel.Req req) {
        return CommonResponse.of(assistantService.runAssistant(req));
    }


}
