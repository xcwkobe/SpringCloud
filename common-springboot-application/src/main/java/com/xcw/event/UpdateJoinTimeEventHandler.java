package com.xcw.event;

import com.xcw.entity.MeetingParticipant;
import com.xcw.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @class: UpdateJoinTimeEventHandler
 * @author: ChengweiXing
 * @description: TODO
 **/
@Component
public class UpdateJoinTimeEventHandler {

    @Autowired
    private ParticipantService participantService;

    @Async
    @EventListener
    public void updateJoinTime(UpdateJoinTimeEvent event){
        MeetingParticipant participant= (MeetingParticipant) event.getSource();
        //更新参会人参会时间
        participantService.updateJoinTime(participant.getMeetingId(),participant.getAccountId(),participant.getJoinTime());

    }
}
