package com.xcw.event;

import org.springframework.context.ApplicationEvent;

/**
 * @class: UpdateJoinTimeEvent
 * @author: ChengweiXing
 * @description: TODO
 **/
public class UpdateJoinTimeEvent extends ApplicationEvent {

    public UpdateJoinTimeEvent(Object source) {
        super(source);
    }
}
