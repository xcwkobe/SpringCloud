package com.xcw.service;

import com.xcw.entity.Meeting;
import com.xcw.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: MeetingService
 * @author: ChengweiXing
 * @description: TODO
 **/
@Service
public class MeetingService {

    @Autowired
    private MeetingMapper meetingMapper;

    public Meeting getById(Long meetingId) {
        return meetingMapper.selectById(meetingId);
    }
}
