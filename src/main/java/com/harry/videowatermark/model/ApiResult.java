package com.harry.videowatermark.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/7/31
 */
@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -3540272309116909111L;
    private static final int ERROR_CODE = -1;
    private static final String ERROR_MSG = "系统错误";
    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MSG = "成功";

    /**
     * 错误码，0代表成功，其他代表失败
     */
    protected int code;

    /**
     * 错误信息，对应错误码
     */
    protected String msg;

    /**
     * 业务数据
     */
    protected T data = (T) Collections.EMPTY_MAP;

    public ApiResult() {
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static final ApiResult FAIL = ApiResult.failed(ERROR_CODE, ERROR_MSG);


    private static <T> ApiResult<T> wrap(int code, String msg, T data) {
        return new ApiResult<T>(code, msg, data);
    }

    public static <T> ApiResult<T> success(T data) {
        return wrap(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> ApiResult<T> success() {
        return wrap(SUCCESS_CODE, SUCCESS_MSG, (T) Collections.emptyMap());
    }

    public static <T> ApiResult<T> failed(int code, String msg) {
        return wrap(code, msg, (T) Collections.emptyMap());
    }

    public static <T> ApiResult<T> failed(String msg) {
        return failed(ERROR_CODE, msg);
    }

    public static <T> ApiResult<T> failed() {
        return failed(ERROR_CODE, ERROR_MSG);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + msg + '\'' +
                ", zpData=" + data +
                '}';
    }

}
