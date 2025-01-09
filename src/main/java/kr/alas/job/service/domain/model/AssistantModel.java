package kr.alas.job.service.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 가진 필드 제외
public class AssistantModel {

    private String uuid;
    private Boolean usedYn;
    private String description;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Req extends AssistantModel {

        private String data;
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
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
    public static class Res extends AssistantModel {

        private String resultData;
        private String AssistantStatus;
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
