package com.example.springbootfilter.filter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/31 10:46 上午
 * @description
 */

@RestController
public class luo2Controller {

    @GetMapping("/test1")
    public String hello(){
        System.out.println("method in controller");
        return "test1";
    }

    /**
     * 用于验证过滤逻辑.参考SecondFilter 过滤之后请求不会到达controller层来
     * @params []
     * @return java.lang.String
     * @author luoyu
     * @date 2021/12/31 10:27 上午
     **/
    @GetMapping("/api/filterTest.json")
    public String abc(){
        System.out.println("method in controller");
        return "filterTest";
    }
}