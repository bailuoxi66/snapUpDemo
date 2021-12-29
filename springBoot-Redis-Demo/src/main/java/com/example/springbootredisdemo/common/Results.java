package com.example.springbootredisdemo.common;

import org.slf4j.MDC;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/29 11:19 上午
 * @description
 */

public class Results {

    public static Result success(Object data) {
        Result result = new Result();
        result.setData(data);
        result.setSuccess(true);
        result.setCode("200");
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode("200");
        return result;
    }

    public static Result fail(ErrorCode errorCode) {
        Result result = new Result();
        result.setError(errorCode);
        result.setSuccess(false);
        return result;
    }

    public static Result fail(String code, String msg) {
        Result result = new Result();
        result.setError(code, msg);
        result.setSuccess(false);
        return result;
    }
}
