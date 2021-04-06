package com.xcw.entity;

public enum MeetingParticipantStatus {

    ACTUAL("实到"),
    ABSENTEES("缺席");

    private String desc;

    MeetingParticipantStatus(String desc) {
        this.desc=desc;
    }
}
