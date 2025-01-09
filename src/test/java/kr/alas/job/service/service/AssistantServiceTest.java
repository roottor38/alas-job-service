package kr.alas.job.service.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kr.alas.job.service.TestConfiguration;
import kr.alas.job.service.domain.entity.Assistant;
import kr.alas.job.service.domain.service.AssistantService;
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
public class AssistantServiceTest {

    @Autowired
    private AssistantService assistantService;

    @Test
    public void getByIdTest() {
        // given
        String uuid = "e95c42e6-12d9-45c4-b337-a59817c3e7df";
//        given(assistantRepo.findByUuid(uuid)).willReturn(Assistant.builder().uuid(uuid).build());

        // when

        // then
    }

}
