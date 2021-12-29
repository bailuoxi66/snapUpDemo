package com.example.springbootredisdemo.common;

public enum ErrorCode implements CodeEnum<String>{
    /**
     * 系统错误
     */
    SYS_ERROR(MsgCode.E0100001, "system error, please contact admin"),

    /**
     * 未登入
     */
    UNLOGIN(MsgCode.E0100002, "please login"),

    /**
     * 系统资源不正常
     */
    SYS_RESOURCE_ERROR(MsgCode.E0100003, "sys resource error"),
    ;

    private final String code;
    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(String... args) {
        return String.format(msg, args);
    }
}
