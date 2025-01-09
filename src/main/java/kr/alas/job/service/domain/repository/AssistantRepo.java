package kr.alas.job.service.domain.repository;

import java.util.Optional;
import kr.alas.job.service.domain.entity.Assistant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantRepo extends JpaRepository<Assistant, String> {

    Page<Assistant> findAll(Specification<Assistant> spec, Pageable pageable);

    Optional<Assistant> findByUuid(String uuid);

}
