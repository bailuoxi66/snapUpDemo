package com.example.springbootredisdemo.redPacket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootredisdemo.common.RedisPool;
import com.example.springbootredisdemo.common.Result;
import com.example.springbootredisdemo.common.Results;
import com.example.springbootredisdemo.redPacket.service.RedPacService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/30 11:56 上午
 * @description
 */

@Slf4j
@Service
public class RedPacServiceImpl implements RedPacService {

    static int honBaoCount = 1_0_000;

    static int threadCount = 20;

    static String hongBaoList = "hongBaoList";
    static String hongBaoConsumedList = "hongBaoConsumedList";
    static String hongBaoConsumedMap = "hongBaoConsumedMap";

    static Random random = new Random();


//	-- 函数：尝试获得红包，如果成功，则返回json字符串，如果不成功，则返回空
//	-- 参数：红包队列名， 已消费的队列名，去重的Map名，用户ID
//	-- 返回值：nil 或者 json字符串，包含用户ID：userId，红包ID：id，红包金额：money
    static String tryGetHongBaoScript =
            "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"
                    + "return nil\n"
                    + "else\n"
                    + "local hongBao = redis.call('rpop', KEYS[1]);\n"
                    + "if hongBao then\n"
                    + "local x = cjson.decode(hongBao);\n"
                    + "x['userId'] = KEYS[4];\n"
                    + "local re = cjson.encode(x);\n"
                    + "redis.call('hset', KEYS[3], KEYS[4], KEYS[4]);\n"
                    + "redis.call('lpush', KEYS[2], re);\n"
                    + "return re;\n"
                    + "end\n"
                    + "end\n"
                    + "return nil";
    static StopWatch watch = new StopWatch();


    @Override
    public Result grab() throws InterruptedException {
        generateTestData();
        testTryGetHongBao();
        return Results.success();
    }

    /**
     * 生成未消费队列数据【生成指定数量的红包id和红包】
     * 这个函数，根据具体的参数：总共需要生成100000个红包,20个线程一起生成。每个线程生成500个红包相关的数据
     * @params []
     * @return void
     * @author luoyu
     * @date 2021/12/30 2:07 下午
     **/
    public void generateTestData() throws InterruptedException {
        Jedis jedis = RedisPool.getJedis();
        jedis.flushAll();
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i = 0; i < threadCount; ++i) {
            final int temp = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Jedis jedis = RedisPool.getJedis();

                    //这里的目的是每个线程生成指定数量的数据，通过jedis.lpush填充到指定的队列里面
                    int per = honBaoCount/threadCount;
                    JSONObject object = new JSONObject();
                    for(int j = temp * per; j < (temp+1) * per; j++) {
                        object.put("id", j);
                        object.put("money", j);

                        System.out.println(object.toJSONString());
                        //填充到jedis的指定队列里面
                        jedis.lpush(hongBaoList, object.toJSONString());
                    }
                    latch.countDown();
                }
            };
            thread.start();
        }
        latch.await();
    }

    /**
     *
     * @params []
     * @return void
     * @author luoyu
     * @date 2021/12/30 2:11 下午
     **/
    static public void testTryGetHongBao() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(threadCount);
        System.err.println("start:" + System.currentTimeMillis()/1000);
        watch.start();
        for(int i = 0; i < threadCount; ++i) {
            final int temp = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Jedis jedis = RedisPool.getJedis();
                    int j = honBaoCount/threadCount * temp;
                    while(true) {
                        //开始取红包
                        Object object = jedis.eval(tryGetHongBaoScript, 4, hongBaoList, hongBaoConsumedList, hongBaoConsumedMap, "" + j);
                        j++;
                        if (object != null) {
							System.out.println("get hongBao:" + object);
                        }else {
                            //已经取完了
                            if(jedis.llen(hongBaoList) == 0) {
                                break;
                            }
                        }
                    }
                    latch.countDown();
                }
            };
            thread.start();
        }

        latch.await();
        watch.stop();

        System.err.println("time:" + watch.getTotalTimeSeconds());
        System.err.println("speed:" + honBaoCount/watch.getTotalTimeSeconds());
        System.err.println("end:" + System.currentTimeMillis()/1000);
    }
}
