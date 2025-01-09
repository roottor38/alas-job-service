package kr.alas.job.service.domain.service;

import static kr.alas.job.service.common.data.Constants.DEFAULT_DATE_FORMAT;
import static kr.alas.job.service.common.data.Constants.TIME_STAMP_PATTERN;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kr.alas.job.service.common.data.AuthType;
import kr.alas.job.service.common.data.YN;
import kr.alas.job.service.common.exception.CustomException;
import kr.alas.job.service.common.exception.ErrorSpec;
import kr.alas.job.service.common.model.PageParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.util.ObjectUtils;

public interface FilterSpecification {

    default <T> Specification<T> filter(PageParam pageParam) {
        return filter(pageParam, false);
    }

    default <T> Specification<T> filter(PageParam pageParam, boolean includeDeleted) {
        Specification<T> spec = null;
        spec = dateBetween(spec, pageParam);
        for (Pair<String, List<String>> filter : pageParam.getFilters()) {
            spec = inValues(spec, filter.getFirst(), Arrays.asList(filter.getSecond().get(0).split(",")));
        }
        return spec;
    }

    default <T> Specification<T> isNull(final String column) {
        return (root, query, cb) -> cb.isNull(root.get(column));
    }

    default <T> Specification<T> isNull(Specification<T> spec, String column) {
        return Objects.isNull(spec) ? Specification.where(isNull(column)) : spec.and(isNull(column));
    }

    default <T> Specification<T> in(final String key, final Object value) {
        return (root, query, cb) -> root.get(key).in(value);
    }

    default <T> Specification<T> in(Specification<T> spec, String key, Object value) {
        return Objects.isNull(spec) ? Specification.where(in(key, value)) : spec.and(in(key, value));
    }

    default <T> Specification<T> equal(final String key, final Object value) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
//            if (keys.length > 2) throw ServiceException.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "equal Error");

            return (root, query, cb) -> cb.equal(root.get(keys[0]).get(keys[1]), value);
        } else {
            return (root, query, cb) -> cb.equal(root.get(key), value);
        }
    }

    default <T> Specification<T> equal(Specification<T> spec, final String key, final Object value) {
        return Objects.isNull(spec) ? Specification.where(equal(key, value)) : spec.and(equal(key, value));
    }

    default <T> Specification<T> notEqual(final String key, final Object value) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
//            if (keys.length > 2) throw ServiceException.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "notEqual Error");

            return (root, query, cb) -> cb.notEqual(root.get(keys[0]).get(keys[1]), value);
        } else {
            return (root, query, cb) -> cb.notEqual(root.get(key), value);
        }
    }

    default <T> Specification<T> notEqual(Specification<T> spec, String key, Object value) {
        return Objects.isNull(spec) ? Specification.where(notEqual(key, value)) : spec.and(notEqual(key, value));
    }

    default <T> Specification<T> equalActive() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), YN.Y);
    }

    default <T> Specification<T> or(Specification<T> spec, final String key, final Object value) {
        return Objects.isNull(spec) ? Specification.where(notEqual(key, value)) : spec.or(notEqual(key, value));
    }

    default <T, S> Specification<T> inValues(Specification<T> spec, String key, List<S> values) {
        return (Objects.isNull(values) || values.isEmpty()) ? spec :
            (Objects.isNull(spec) ? Specification.where(inValues(key, values)) : spec.and(inValues(key, values)));
    }

    default <T, S> Specification<T> inValues(final String key, final List<S> values) {
        return (root, query, cb) -> {
            Path<?> path = root;
            String[] nested = key.split("\\.");
            for (String it : nested) {
                path = path.get(it);
            }
            List<Predicate> predicates = new ArrayList<>();
            if (String.class.isAssignableFrom(path.getJavaType())) {
                Path<String> stringPath = (Path<String>) path;
                values.forEach(v -> {
                    predicates.add(cb.like(stringPath, "%" + v + "%"));
                });
            } else if (YN.class.isAssignableFrom(path.getJavaType())) {
                List<YN> ynValues = values.stream().map(v -> YN.of(String.valueOf(v))).collect(Collectors.toList());
                predicates.add(path.in(ynValues));
            } else if (AuthType.class.isAssignableFrom(path.getJavaType())) {
                List<AuthType> authValues = values.stream()
                    .map(v -> AuthType.of(Integer.valueOf(String.valueOf(v))))
                    .collect(Collectors.toList());
                predicates.add(path.in(authValues));
            } else {
                predicates.add(path.in(values));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    default <T, S> Specification<T> compareValue(Specification<T> spec, String key, List<S> values) {
        return (Objects.isNull(values) || values.isEmpty()) ? spec :
            (Objects.isNull(spec) ? Specification.where(compareValue(key, values)) : spec.and(compareValue(key, values)));
    }

    default <T, S> Specification<T> compareValue(final String key, final List<S> values) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            values.forEach(v ->
                predicates.add(Long.parseLong(v.toString()) == 0
                    ? cb.equal(root.get(key), 0)
                    : (Long.parseLong(v.toString()) > 0) ? cb.greaterThan(root.get(key), 0) : cb.lessThan(root.get(key), 0)
                ));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    default <T, S> Specification<T> greaterThanOrNullDate(Specification<T> spec, String key, String value) {
        try {
            Timestamp time = new Timestamp(new SimpleDateFormat(TIME_STAMP_PATTERN).parse(value).getTime());
            return (Objects.isNull(value) || value.isEmpty()) ? spec :
                (Objects.isNull(spec) ? Specification.where(greaterThanOrNullDate(key, time)) : spec.and(greaterThanOrNullDate(key, time)));
        } catch (ParseException e) {
            return spec;
        }
    }

    default <T, S> Specification<T> greaterThanOrNullDate(final String key, Timestamp value) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.greaterThan(root.get(key), value));
            predicates.add(cb.isNull(root.get(key)));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    default <T, S> Specification<T> greaterThanDate(Specification<T> spec, String key, String value) {
        try {
            Timestamp time = new Timestamp(new SimpleDateFormat(TIME_STAMP_PATTERN).parse(value).getTime());
            return (Objects.isNull(value) || value.isEmpty()) ? spec :
                (Objects.isNull(spec) ? Specification.where(greaterThanDate(key, time)) : spec.and(greaterThanDate(key, time)));
        } catch (ParseException e) {
            return spec;
        }
    }

    default <T, S> Specification<T> greaterThanDate(final String key, Timestamp value) {
        return (root, query, cb) -> cb.greaterThan(root.get(key), value);
    }

    default <T, S> Specification<T> lessThanDate(Specification<T> spec, String key, String value) {
        try {
            Timestamp time = new Timestamp(new SimpleDateFormat(TIME_STAMP_PATTERN).parse(value).getTime());
            return (Objects.isNull(value) || value.isEmpty()) ? spec :
                (Objects.isNull(spec) ? Specification.where(lessThanDate(key, time)) : spec.and(lessThanDate(key, time)));
        } catch (ParseException e) {
            return spec;
        }
    }

    default <T, S> Specification<T> lessThanDate(final String key, final Timestamp value) {
        return (root, query, cb) -> cb.lessThan(root.get(key), value);
    }

    default <T> Specification<T> dateBetween(Specification<T> spec, PageParam pageParam) {
        if (Objects.isNull(pageParam.getDateKey())) {
            return spec;
        } else if (!pageParam.getFrom().contains(":")) {
            return periodDateBetween(pageParam.getDateKey(), spec, pageParam.getFrom(), pageParam.getTo());
        }
        return periodBetween(pageParam.getDateKey(), spec, pageParam.getFrom(), pageParam.getTo());
    }

//    default <T> Specification<T> periodBetween(String key, final Timestamp from, final Timestamp to) {
//        return (root, query, cb) -> cb.between(root.get(key), from, to);
//    }

    default <T> Specification<T> periodBetween(String key, final java.util.Date from, final Timestamp to) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length > 2) {
                throw CustomException.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "periodBetween Error");
            }
            return (root, query, cb) -> cb.between(root.get(keys[0]).get(keys[1]), from, to);
        } else {
            return (root, query, cb) -> cb.between(root.get(key), from, to);
        }
    }

    default <T> Specification<T> periodBetween(String key, final Date from, final Date to) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length > 2) {
                throw CustomException.of(ErrorSpec.ERROR_SPEC_Invalid_Parameter, "periodBetween Error");
            }
            return (root, query, cb) -> cb.between(root.get(keys[0]).get(keys[1]), from, to);
        } else {
            return (root, query, cb) -> cb.between(root.get(key), from, to);
        }
    }

    default <T> Specification<T> periodBetween(String key, Specification<T> spec, String from, String to) {
        if (ObjectUtils.isEmpty(from) || ObjectUtils.isEmpty(to)) {
            return spec;
        } else {
            Timestamp fromTime;
            Timestamp toTime;

            try {
                fromTime = new Timestamp(DEFAULT_DATE_FORMAT.parse(from).getTime() - TimeUnit.SECONDS.toMillis(1));
                toTime = new Timestamp(DEFAULT_DATE_FORMAT.parse(to).getTime() + TimeUnit.DAYS.toMillis(1));
            } catch (ParseException e) {
                return spec;
            }

            if (Objects.isNull(spec)) {
                return Specification.where(periodBetween(key, fromTime, toTime));
            } else {
                return spec.and(periodBetween(key, fromTime, toTime));
            }
        }
    }

    default <T> Specification<T> periodDateBetween(String key, Specification<T> spec, String from, String to) {
        if (ObjectUtils.isEmpty(from) || ObjectUtils.isEmpty(to)) {
            return spec;
        } else {
            Date fromDate;
            Date toDate;

            try {
                fromDate = new Date(DEFAULT_DATE_FORMAT.parse(from).getTime());
                toDate = new Date(DEFAULT_DATE_FORMAT.parse(to).getTime());
            } catch (ParseException e) {
                return spec;
            }

            if (Objects.isNull(spec)) {
                return Specification.where(periodBetween(key, fromDate, toDate));
            } else {
                return spec.and(periodBetween(key, fromDate, toDate));
            }
        }
    }
}
