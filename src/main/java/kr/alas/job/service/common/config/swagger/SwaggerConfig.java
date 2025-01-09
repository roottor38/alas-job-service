package kr.alas.job.service.common.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition(
//    info = @Info(
//        title = "Job Service",
//        version = "v1",
//        description = "Job Service API"
//    )
//)
//@RequiredArgsConstructor
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/**"};
//        return GroupedOpenApi.builder()
//            .group("job")
//            .pathsToMatch(paths)
//            .build();
//    }
//
//}
