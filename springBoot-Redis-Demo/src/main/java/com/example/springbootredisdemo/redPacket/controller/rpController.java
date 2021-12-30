package com.example.springbootredisdemo.redPacket.controller;

import com.example.springbootredisdemo.common.Result;
import com.example.springbootredisdemo.redPacket.service.RedPacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/30 11:50 上午
 * @description
 */
@RestController
@RequestMapping("api/redpack")
public class rpController {

    @Autowired
    private RedPacService redPacService;

    /**
     * 抢红包
     * @params []
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/30 12:20 下午
     **/
    @GetMapping("grab.json")
    public Result grab() throws InterruptedException {
        return redPacService.grab();
    }
}
