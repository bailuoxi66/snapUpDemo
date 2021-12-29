package com.example.springbootredisdemo.secKill.service;

import com.example.springbootredisdemo.common.Result;
import com.example.springbootredisdemo.secKill.model.req.RedisInfoReq;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/28 8:35 下午
 * @description
 */

public interface RedisService {

    Result set(RedisInfoReq redisInfoReq);

    Result get(String key);

    Result setnx(RedisInfoReq redisInfoReq);

    Result delnx(RedisInfoReq redisInfoReq);

    Result snapUp();
}
