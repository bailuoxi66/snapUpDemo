package com.example.springbootredisdemo.secKill.model.req;

import lombok.*;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/29 11:23 上午
 * @description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisInfoReq {

    private String key;

    private String value;
}
