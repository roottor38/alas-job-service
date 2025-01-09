package kr.alas.job.service.common.component;

import kr.alas.job.service.domain.model.AssistantWorkHistModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, AssistantWorkHistModel> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, AssistantWorkHistModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, AssistantWorkHistModel message) {
        kafkaTemplate.send(topic, message);
        log.info("Message sent to topic {}: {}", topic, message);
    }


}
