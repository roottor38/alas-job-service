package kr.alas.job.service.service;

import kr.alas.job.service.TestConfiguration;
import kr.alas.job.service.common.component.KafkaConsumer;
import kr.alas.job.service.common.component.KafkaProducer;
import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.model.AssistantModel;
import kr.alas.job.service.domain.model.AssistantModel.Res;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringJUnitConfig
@WebAppConfiguration
@ContextConfiguration(
    initializers = ConfigDataApplicationContextInitializer.class,
    classes = {TestConfiguration.class})
public class KafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void setKafkaProducer() {
        AssistantModel.Res test = AssistantModel.Res.of(Assistant.builder().uuid("test").build());

        kafkaProducer.sendMessage("assistant-topic", test);

    }

    @Test
    public void get() {

    }

}
