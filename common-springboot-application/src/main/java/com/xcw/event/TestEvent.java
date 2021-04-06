package com.xcw.event;

import org.springframework.context.ApplicationEvent;

/**
 * @class: TestEvent
 * @author: ChengweiXing
 * @description: TODO
 **/
public class TestEvent extends ApplicationEvent {

    public TestEvent(Object source) {
        super(source);
    }
}