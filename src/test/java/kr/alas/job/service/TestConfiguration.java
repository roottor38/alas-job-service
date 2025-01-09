package kr.alas.job.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.Executor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ImportAutoConfiguration(
    classes = {
      ObjectMapper.class,
    })
@ComponentScan(basePackages = {"kr.alas.job.service"})
public class TestConfiguration implements Executor {
  @Override
  public void execute(@NotNull Runnable runnable) {}
}
