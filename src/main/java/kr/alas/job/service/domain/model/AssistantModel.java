package kr.alas.job.service.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.alas.job.service.domain.entity.Assistant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistantModel {

    @Schema(description = "Assistant UUID")
    private String uuid;
    @Schema(description = "사용여부")
    private Boolean usedYn;
    @Schema(description = "Assistant 설명")
    private String description;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @Schema(name = "AssistantRequest", description = "Assistant 작업 요청")
    public static class Req extends AssistantModel {
        @Schema(description = "Assistant 작업 요청 데이터(사각형 영역 또는 Line 정보)")
        private String data;
        @Schema(description = "Assistant 작업 요청 이미지 S3 URL")
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @Schema(name = "AssistantPageResponse", description = "Assistant page 목록 조회 응답")
    public static class ResPage extends AssistantModel {
        private String imageUrl;

        public static ResPage of(Assistant assistantModel) {
            return ResPage.builder()
                .uuid(assistantModel.getUuid())
                .usedYn(assistantModel.getUsedYn())
                .description(assistantModel.getDescription())
                .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @Schema(name = "AssistantResponse", description = "Assistant 작업 응답")
    public static class Res extends AssistantModel {

        @Schema(description = "Assistant 작업 결과 데이터")
        private String resultData;
        @Schema(description = "Assistant 작업 상태")
        private String AssistantStatus;
        @Schema(description = "Assistant 작업 결과 이미지 S3 URL")
        private String imageUrl;

        public static Res of(Assistant assistantModel) {
            return Res.builder()
                .uuid(assistantModel.getUuid())
                .usedYn(assistantModel.getUsedYn())
                .description(assistantModel.getDescription())
                .AssistantStatus("success")
                .imageUrl("http://localhost:8080/image/1")
                .resultData("result data")
                .build();
        }
    }
}
