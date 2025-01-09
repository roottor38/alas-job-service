package kr.alas.job.service.domain.service;

import jakarta.transaction.Transactional;
import kr.alas.job.service.common.component.KafkaProducer;
import kr.alas.job.service.common.model.PageParam;
import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.model.AssistantModel;
import kr.alas.job.service.domain.model.AssistantWorkHistModel;
import kr.alas.job.service.domain.repository.AssistantRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AssistantService implements FilterSpecification{
    private final AssistantRepo assistantRepo;
    private final KafkaProducer kafkaProducer;

    public Page<Assistant> getPage() {
        PageParam pageParam = PageParam.fromRequest();
        Specification<Assistant> spec = filter(pageParam);
        PageRequest result = pageParam.getPageRequest();
        return assistantRepo.findAll(spec, result);
    }

    public Assistant getAssistant(String uuid) {
        return assistantRepo.findByUuid(uuid);
    }

    public AssistantModel.Res runAssistant(final AssistantModel.Req req) {
        //TODO : run assistant
        log.info("start assistant : {}", req);

        Assistant assistant = assistantRepo.findByUuid(req.getUuid());
        AssistantWorkHistModel hist = AssistantWorkHistModel.of(assistant);
        AssistantModel.Res res =  AssistantModel.Res.of(assistant);

        log.info("end assistant-{}: {}", req.getUuid(), res.getAssistantStatus());

        kafkaProducer.sendMessage("assistant-topic", hist);
        return res;
    }


}
