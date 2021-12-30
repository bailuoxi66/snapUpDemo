package com.example.springbootredisdemo.secKill.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.springbootredisdemo.common.ErrorCode;
import com.example.springbootredisdemo.common.Result;
import com.example.springbootredisdemo.common.Results;
import com.example.springbootredisdemo.common.RedisPool;
import com.example.springbootredisdemo.secKill.model.req.RedisInfoReq;
import com.example.springbootredisdemo.secKill.service.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/28 8:36 下午
 * @description
 */

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    //获取锁的超时时间
    private int timeout = 30*1000;

    //商品key名字
    private String shangpingKey = "computer_key";

    //总库存
    private Integer Stock = 0;


    @Override
    public Result set(RedisInfoReq redisInfoReq) {
        log.warn("RedisServiceImpl set req:{}", JSON.toJSONString(redisInfoReq));
        RedisPool.getJedis().set(redisInfoReq.getKey(), redisInfoReq.getValue());
        return Results.success();
    }

    @Override
    public Result get(String key) {
        log.warn("RedisServiceImpl get req:{}", key);
        String res = RedisPool.getJedis().get(key);
        return Results.success(res);
    }

    @Override
    public Result setnx(RedisInfoReq redisInfoReq) {

        //log.warn("RedisServiceImpl setnx req:{}", JSON.toJSONString(redisInfoReq));

        Jedis jedis = RedisPool.getJedis();

        try {
            if (jedis == null){
                return Results.fail(ErrorCode.SYS_RESOURCE_ERROR.getCode(), "setnx jedis is null");
            }

            //这里注意点在于jedis的set方法
            //NX：是否存在key，存在就不set成功
            //PX：key过期时间单位设置为毫秒（EX：单位秒）
            String res = jedis.set(redisInfoReq.getKey(), redisInfoReq.getValue(), "NX", "PX", 1000 * 60);
            return res == null? Results.fail(ErrorCode.SYS_ERROR.getCode(), "res == null"): Results.success(res);
        } catch (Exception e){
            e.printStackTrace();
            log.warn("setnx exception:{}", e.getMessage());
            return Results.fail(ErrorCode.SYS_ERROR.getCode(), e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    /**
     * delnx : 使用了jedis方式，直接执行lua脚本：根据val判断其是否存在，如果存在就del
     * @params [redisInfoReq]
     * @return com.example.springbootredisdemo.common.Result
     * @author luoyu
     * @date 2021/12/29 1:48 下午
     **/
    @Override
    public Result delnx(RedisInfoReq redisInfoReq) {
        log.warn("RedisServiceImpl delnx req:{}", JSON.toJSONString(redisInfoReq));
        Jedis jedis = RedisPool.getJedis();
        String key = redisInfoReq.getKey();
        String val = redisInfoReq.getValue();
        try {
            if (jedis == null){
                return Results.success(0);
            }
            StringBuilder sbScript = new StringBuilder();
            sbScript.append("if redis.call('get','").append(key).append("')").append("=='").append(val).append("'").
                    append(" then ").
                    append("    return redis.call('del','").append(key).append("')").
                    append(" else ").
                    append("    return 0").
                    append(" end");

            return Results.success(Integer.valueOf(jedis.eval(sbScript.toString()).toString()));
        } catch (Exception e){
            e.printStackTrace();
            log.warn("setnx exception:{}", e.getMessage());
            return Results.fail(ErrorCode.SYS_ERROR.getCode(), e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public Result snapUp() {

        //抢到商品的用户
        List<String> shopUsers = new ArrayList<>();

        //构造很多的用户
        List<String> users = new ArrayList<>();
        IntStream.range(0, 1000).parallel().forEach(b -> {
            users.add("luo-" + b);
        });

        log.warn("user:{}", JSON.toJSONString(users));

        //初始化库存
        Stock = 10;
        users.parallelStream().forEach(b -> {
            String shopUser = rushPurchase(b);
            if (!StringUtils.isEmpty(shopUser)){
                shopUsers.add(shopUser);
            }
        });

        return Results.success(shopUsers);
    }

    private String rushPurchase(String user){
        //用户开抢时间
        long startTime = System.currentTimeMillis();
        //未抢到的情况下，30秒内继续获取锁
        while ((startTime + timeout) >= System.currentTimeMillis()) {
            //商品是否剩余
            if (Stock <= 0) {
                break;
            }

            RedisInfoReq build = RedisInfoReq.builder().key(shangpingKey).value(user).build();

            //该用户获取锁成功
            if (setnx(build).isSuccess()) {
                //用户user拿到锁
                log.warn("用户{}拿到锁...", user);
                try {
                    //商品是否剩余
                    if (Stock <= 0) {
                        break;
                    }

                    //模拟生成订单耗时操作, 刻意进行延时操作
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //抢购成功，商品递减，记录用户
                    Stock -= 1;

                    //抢单成功跳出
                    log.warn("用户{}抢单成功跳出...所剩库存：{}", user, Stock);

                    return user + "抢单成功，所剩库存：" + Stock;
                } finally {
                    log.warn("用户{}释放锁...", user);
                    //释放锁
                    delnx(build);
                }
            }
        }
        return "";
    }
}
