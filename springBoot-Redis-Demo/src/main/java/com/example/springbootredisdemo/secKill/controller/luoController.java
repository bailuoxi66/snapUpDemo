package com.example.springbootredisdemo.secKill.controller;

import com.example.springbootredisdemo.common.Result;
import com.example.springbootredisdemo.secKill.model.req.RedisInfoReq;
import com.example.springbootredisdemo.secKill.service.RedisService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/28 8:15 下午
 * @description 秒杀系统整理
 */

@RestController
@RequestMapping("api/redis")
public class luoController {

    @Autowired
    private RedisService redisService;

    @ResponseBody
    @RequestMapping("hello.json")
    public String hello(){
        return "Hello World!";
    }

    /**
     * 单线程场景下的 set操作
     * @params [redisInfoReq]
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/29 11:39 上午
     **/
    @PostMapping("set.json")
    public Result set(@RequestBody @Validated RedisInfoReq redisInfoReq){
        return redisService.set(redisInfoReq);
    }

    /**
     * 单线程场景下的 get操作
     * @params [key]
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/29 11:40 上午
     **/
    @GetMapping("get.json")
    public Result get(@NonNull @RequestParam("key") String key){
        return redisService.get(key);
    }

    /**
     * 创建锁的策略：redis的普通key一般都允许覆盖，A用户set某个key后，B在set相同的key时同样能成功，如果是锁场景，那就无法知道到底是哪个用户set成功的；
     * 这里jedis的setnx方式为我们解决了这个问题，简单原理是：当A用户先set成功了，那B用户set的时候就返回失败，满足了某个时间点只允许一个用户拿到锁。
     *
     * 锁过期时间：某个抢购场景时候，如果没有过期的概念，当A用户生成了锁，但是后面的流程被阻塞了一直无法释放锁，那其他用户此时获取锁就会一直失败，
     * 无法完成抢购的活动；当然正常情况一般都不会阻塞，A用户流程会正常释放锁；过期时间只是为了更有保障。
     *
     * @params [redisInfoReq]
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/29 11:40 上午
     **/
    @PostMapping("setnx.json")
    public Result setnx(@RequestBody @Validated RedisInfoReq redisInfoReq){
        return redisService.setnx(redisInfoReq);
    }

    /**
     * 注意的是delnx时，需要传递创建锁时的value，因为通过get的value与delnx的value来判断是否是持有锁的操作请求，只有value一样才允许del；
     * @params [redisInfoReq]
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu        
     * @date 2021/12/29 1:45 下午
     **/
    @PostMapping("delnx.json")
    public Result delnx(@RequestBody @Validated RedisInfoReq redisInfoReq){
        return redisService.delnx(redisInfoReq);
    }

    /**
     * 抢购
     * @params []
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/29 1:51 下午
     **/
    @PostMapping("snapUp.json")
    public Result snapUp(){
        return redisService.snapUp();
    }
}
