package com.example.springbootfilter.filter.controller;

import com.example.springbootfilter.filter.model.req.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    /**
     * 登陆-session filter验证
     * @params [name, pwd, request]
     * @return java.lang.String
     * @author luoyu
     * @date 2021/12/31 12:05 下午
     **/
    @RequestMapping("login")
    public String login(String name, String pwd, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(name.equals("root")&&pwd.equals("root")) {
            User user = new User();
            user.setName(name);
            session.setAttribute("user",user);
            return "登录成功";
        } else {
            return "用户名或密码错误!";
        }
    }
}