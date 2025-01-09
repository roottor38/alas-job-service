package kr.alas.job.service.common.response;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> extends Response {

    private HashMap<String, T> data;
    private Long total;

    public CommonResponse() {
        super();
        data = new HashMap<>();
    }

    private CommonResponse(HashMap<String, T> data) {
        this();
        this.data.putAll(data);
    }
    public CommonResponse<T> data(HashMap<String, T> data) {
        setData(data);
        return this;
    }

    public CommonResponse<T> putData(String key, T value) {
        data.put(key, value);
        return this;
    }

    public CommonResponse<T> putList( T list, Long total) {
        data.put("list", list);
        this.total = total;
        return this;
    }

    public CommonResponse<T> putResult(T value) {
        data.put("result", value);
        return this;
    }

    public CommonResponse<T> putMsgDto(T value) {
        data.put("MsgDto", value);
        return this;
    }

    public CommonResponse<T> putMessage(T msg) {
        data.put("message", msg);
        return this;
    }

    public static <T> CommonResponse<T> ofList(T list, Long total) {
        return new CommonResponse<T>().putList(list, total);
    }

    public static <T> CommonResponse<T> of(T value) {
        return new CommonResponse<T>().putResult(value);
    }

}
