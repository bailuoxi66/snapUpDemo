package com.example.springbootuserfilter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/31 10:46 上午
 * @description
 */

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String hello(){
        System.out.println("method in controller");
        return "hello";
    }
}