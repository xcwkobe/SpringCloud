package com.xcw.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @class: TestEventHandler
 * @author: ChengweiXing
 * @description: TODO
 **/
@Component
public class TestEventHandler {

    @Async
    @EventListener
    public void test(TestEvent event){
        System.out.println(event);
    }
}
