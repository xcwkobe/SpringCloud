package com.xcw.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xcw.entity.MeetingParticipant;
import com.xcw.entity.MeetingParticipantStatus;
import com.xcw.mapper.ParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @class: ParticipantService
 * @author: ChengweiXing
 * @description: TODO
 **/
@Service
public class ParticipantService {

    @Autowired
    private ParticipantMapper participantMapper;

    public void updateJoinTime(Long meetingId, Long accountId, Date joinTime) {
        MeetingParticipant participant = new MeetingParticipant();
        participant.setAccountId(accountId);
        participant.setMeetingId(meetingId);
        UpdateWrapper<MeetingParticipant> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(MeetingParticipant::getJoinTime, joinTime)
                .set(MeetingParticipant::getParticipantStatus, MeetingParticipantStatus.ACTUAL)
                .eq(MeetingParticipant::getMeetingId, meetingId)
                .eq(MeetingParticipant::getAccountId, accountId);
        participantMapper.update(participant,updateWrapper);
        //update(updateWrapper);
    }
}
