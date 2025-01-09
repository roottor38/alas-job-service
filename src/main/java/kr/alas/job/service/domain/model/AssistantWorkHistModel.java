package kr.alas.job.service.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.entity.AssistantWorkHist;
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
public class AssistantWorkHistModel {

    private Long id;
    private String uuid;
    private Assistant assistant;
    private String resultData;
    private String imageUrl;
    private String assistantStatus;
    private String user;

    public static AssistantWorkHistModel of(Assistant assistant) {
        return AssistantWorkHistModel.builder()
            .uuid(assistant.getUuid())
            .resultData("data ignore")
            .imageUrl("http://localhost:8080/image/1")
            .assistantStatus("success")
            .assistant(assistant)
            .user("user")
            .build();
    }

    public static AssistantWorkHistModel of(AssistantWorkHist data) {
        return AssistantWorkHistModel.builder()
            .id(data.getId())
            .uuid(data.getAssistant_uuid().getUuid())
            .resultData(data.getResultData())
            .imageUrl(data.getImageUrl())
            .assistantStatus(data.getAssistantStatus())
            .build();
    }

}
