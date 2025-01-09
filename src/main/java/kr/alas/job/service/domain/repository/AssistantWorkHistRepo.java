package kr.alas.job.service.domain.repository;

import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.entity.AssistantWorkHist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantWorkHistRepo extends JpaRepository<AssistantWorkHist, String> {

    Page<AssistantWorkHist> findAll(Specification<AssistantWorkHist> spec, Pageable pageable);

}
