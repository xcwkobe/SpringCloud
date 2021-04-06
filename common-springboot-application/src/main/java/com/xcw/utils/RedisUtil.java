package com.xcw.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @class: RedisUtil
 * @author: ChengweiXing
 * @description: TODO
 **/
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultRedisScript script;

    /**
     * 管道批量插入
     * @param map
     * @param expireTime
     * @return
     */
    public void batchInsertByPipeline(Map<String,String> map, long expireTime){
        List<String> result = redisTemplate.executePipelined((RedisCallback<Long>) connection -> {
            connection.openPipeline();
            map.forEach((key, value) -> {

                connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(expireTime), RedisStringCommands.SetOption.UPSERT);

            });
            return null;
        });
        System.out.println(result);
    }

    public void script(){
        DefaultRedisScript script=new DefaultRedisScript();

    }
}
