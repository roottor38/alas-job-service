package kr.alas.job.service.common.component;

import kr.alas.job.service.domain.model.AssistantWorkHistModel;
import kr.alas.job.service.domain.service.AssistantWorkHistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumer {

    private final AssistantWorkHistService assistantService;

    @KafkaListener(topics = "assistant-topic", groupId = "assistant-group")
    public void listen(ConsumerRecord<String, AssistantWorkHistModel> record) {
        // 메시지 처리
        AssistantWorkHistModel message = record.value();
        log.info("Received message: {}", message);

        // 비즈니스 로직 처리
        processMessage(message);
    }

    private void processMessage(AssistantWorkHistModel message) {
        log.info("Processing message: {}", message);
        assistantService.saveAssistantWorkHist(message);
    }


}
