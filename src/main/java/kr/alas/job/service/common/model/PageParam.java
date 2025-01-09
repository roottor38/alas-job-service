package kr.alas.job.service.common.model;

import static org.springframework.data.domain.Sort.Direction;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.alas.job.service.common.exception.CustomException;
import kr.alas.job.service.common.exception.ErrorSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Data
@ToString
@Builder
@AllArgsConstructor
public class PageParam {
    private int limit;
    private int offset;
    private String sort;
    private Direction order;
    private String dateKey;
    private String from;
    private String to;

    private List<Pair<String, List<String>>> filters;
    private List<Pair<String, List<String>>> excludeFilter;

    public static PageParam fromRequest() {
        HttpServletRequest request = getCurrentRequest();
        validateRequestParameters(request);

        List<String> filterKeys = List.of("limit", "offset", "sort", "order", "dateKey", "from", "to");

        return PageParam.builder()
            .limit(getParameterAsInt(request, "limit", 100))
            .offset(getParameterAsInt(request, "offset", 0))
            .sort(Optional.ofNullable(request.getParameter("sort")).orElse("userId"))
            .order(getParameterAsEnum(request, "order", Direction.class, Direction.DESC))
            .dateKey(request.getParameter("dateKey"))
            .from(request.getParameter("from"))
            .to(request.getParameter("to"))
            .filters(
                request.getParameterMap().keySet().stream()
                    .filter(key -> !filterKeys.contains(key))
                    .map(key -> Pair.of(key, Arrays.asList(request.getParameterValues(key))))
                    .collect(Collectors.toList())
            )
            .build();
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attributes).getRequest();
        }
        throw new IllegalStateException("Current request is not available or not a HttpServletRequest");
    }



    private static void validateRequestParameters(HttpServletRequest request) {
        request.getParameterMap().keySet().forEach(param -> {
            if (request.getParameter(param).contains("%")) {
                throw CustomException.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "특수문자(%)로 검색을 할 수 없습니다.");
            }
        });
    }

    private static int getParameterAsInt(HttpServletRequest request, String param, int defaultValue) {
        return Optional.ofNullable(request.getParameter(param))
            .map(Integer::valueOf)
            .orElse(defaultValue);
    }

    private static <T extends Enum<T>> T getParameterAsEnum(HttpServletRequest request, String param, Class<T> enumType, T defaultValue) {
        return Optional.ofNullable(request.getParameter(param))
            .map(value -> Enum.valueOf(enumType, value))
            .orElse(defaultValue);
    }

    public List<String> getValues(String key) {
        return filters.stream()
                .filter(p -> key.equals(p.getFirst()))
                .map(Pair::getSecond)
                .findAny()
                .orElse(Collections.emptyList()).stream()
                .map(s -> StringUtils.uriDecode(s, StandardCharsets.UTF_8))
                .collect(Collectors.toList());
    }

    public Optional<String> getFirstValue(String key) {
        List<String> values = getValues(key);
        return Optional.ofNullable(values)
                .map(v -> v.isEmpty() ? null : v.getFirst());
    }

    public static PageParam fromRequestWithFilter(String key, List<String> value) {
        PageParam fromRequest = fromRequest();
        fromRequest.filters.add(Pair.of(key, value));
        return fromRequest;
    }

    public void setFilter(String key, List<String> value) {
        this.filters.add(Pair.of(key, value));
    }

    public PageRequest getPageRequest() {
        return PageRequest.of(offset / limit, limit, order, sort);
    }

    public static PageParam fromRequestExcludeFilter(List<String> keys) {
        PageParam fromRequest = fromRequest();
        List<Pair<String, List<String>>> filters = fromRequest.getFilters();
        fromRequest.setFilters(filters.stream().filter(f -> !keys.contains(f.getFirst())).collect(Collectors.toList()));
        fromRequest.setExcludeFilter(filters.stream().filter(f -> keys.contains(f.getFirst())).collect(Collectors.toList()));
        return fromRequest;
    }
}
