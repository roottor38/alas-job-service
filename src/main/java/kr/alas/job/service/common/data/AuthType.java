package kr.alas.job.service.common.data;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AuthType {
    ADMIN(0),               // 운영자
    MANAGER(1),             // 매니저
    USER(2);                // 일반

    @JsonValue
    private final Integer value;

    AuthType(Integer value) {
        this.value = value;
    }

    public static AuthType of(Integer i) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(i))
                .findFirst().orElse(USER);
    }
}
