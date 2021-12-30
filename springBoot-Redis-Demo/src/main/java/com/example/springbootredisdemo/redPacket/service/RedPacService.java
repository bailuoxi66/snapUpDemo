package com.example.springbootredisdemo.redPacket.service;

import com.example.springbootredisdemo.common.Result;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/30 11:54 上午
 * @description
 */

public interface RedPacService {

    Result grab() throws InterruptedException;
}
