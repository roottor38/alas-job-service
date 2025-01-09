package kr.alas.job.service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "ASSISTANT")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Assistant extends BaseEntity {

    @Id
    @Column(name = "uuid" , length = 36)
    private String uuid;

    @Column(name = "used_yn" , columnDefinition = "boolean default false", nullable = false)
    private Boolean usedYn;

    @Column(name = "description")
    private String description;

}
