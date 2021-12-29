package com.example.springbootredisdemo.common;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/29 11:11 上午
 * @description
 */

public interface MsgCode {

    Object[] EMPTY_PARAMS = new Object[0];

    /**
     * E01 系统相关
     */
    String SYSTEM_PREFIX  = "E01";

    /**
     * E02 处罚相关
     */
    String PUNISH_PREFIX  = "E02";

    /**
     * E02 业务相关
     */
    String BUSINESS_PREFIX  = "E03";

    /**
     * 系统错误
     */
    String E0100001 = SYSTEM_PREFIX + "000001";

    /**
     * 用户未登入
     */
    String E0100002 = SYSTEM_PREFIX + "000002";

    /**
     * 系统资源不正常
     */
    String E0100003 = SYSTEM_PREFIX + "000003";

}
