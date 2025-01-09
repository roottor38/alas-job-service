package kr.alas.job.service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.alas.job.service.domain.model.AssistantWorkHistModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Table(name = "ASSISTANT_WORK_HIST")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantWorkHist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_uuid", nullable = false)
    private Assistant assistant_uuid;

    @Column(name = "result_data" , nullable = false)
    private String resultData;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "assistant_status", nullable = false)
    private String assistantStatus;

    public static AssistantWorkHist of(AssistantWorkHistModel data) {
        return AssistantWorkHist.builder()
            .id(data.getId())
            .assistant_uuid(data.getAssistant())
            .resultData(data.getResultData())
            .imageUrl(data.getImageUrl())
            .assistantStatus(data.getAssistantStatus())
            .createdBy(data.getUser())
            .updatedBy(data.getUser())
            .build();
    }

}
