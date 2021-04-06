package com.xcw.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;

import java.util.TimerTask;

/**
 * @class: RedisTask
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class RedisTask extends TimerTask {

    private RedisConnection connection;

    private long offset;

    private String pointKey;

    private String offsetKey;

    private boolean pause;

    public void setPause(boolean pause){
        this.pause=pause;
    }

    public RedisTask(RedisConnection connection,String pointKey,long offset,String offsetKey) {
        this.connection = connection;
        this.offset=offset;
        this.pointKey=pointKey;
        this.offsetKey=offsetKey;
    }

    @Override
    public void run() {
        //offset：每定时执行一次加一
        if (!pause){
            connection.set(offsetKey.getBytes(),String.valueOf(offset).getBytes());
            connection.setBit(pointKey.getBytes(),offset,true);
            offset++;
            log.info("定时打点执行中....");
            log.info("offset [{}]", offset);
            log.info("offsetKey [{}]", offsetKey);
            log.info("pointKey [{}]", pointKey);
        }
    }
}
