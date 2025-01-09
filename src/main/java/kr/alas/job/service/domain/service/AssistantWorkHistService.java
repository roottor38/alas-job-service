package kr.alas.job.service.domain.service;

import jakarta.transaction.Transactional;
import kr.alas.job.service.domain.entity.AssistantWorkHist;
import kr.alas.job.service.domain.model.AssistantWorkHistModel;
import kr.alas.job.service.domain.repository.AssistantWorkHistRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AssistantWorkHistService implements FilterSpecification{
    private final AssistantWorkHistRepo assistantWorkHistRepo;

    public void saveAssistantWorkHist(final AssistantWorkHistModel req) {
        log.info("save assistant work history : {}", req);

        AssistantWorkHist hist = AssistantWorkHist.of(req);
        assistantWorkHistRepo.save(hist);
    }

}
