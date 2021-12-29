package com.example.springbootredisdemo.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/29 11:08 上午
 * @description
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8572794469397752445L;

    /**
     * 标识调用是否成功
     */
    private boolean success;

    /**
     * 调用返回code，一般为错误代码
     */
    private String code;

    /**
     * 调用返回的消息，一般为错误消息
     */
    private String msg;

    /**
     * 调用返回的数据
     */
    private T data;

    /**
     * 响应耗时
     */
    private Long rt;

    private boolean fromCache = false;


    public Result() {
        this.success = true;
        this.code = "200";
    }

    public Result(ErrorCode error) {
        this(error.getCode(), "");
    }

    public Result(String code, String msg) {
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public void setError(String code, String msg) {
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public void setError(ErrorCode error) {
        this.setError(error.getCode(), error.getMsg());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    public Long getRt() {
        return rt;
    }

    public void setRt(Long rt) {
        this.rt = rt;
    }


    @Override
    public String toString() {
        return "Result [success=" + success + ", code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}