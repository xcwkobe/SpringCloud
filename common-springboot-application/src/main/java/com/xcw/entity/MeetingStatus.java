package com.xcw.entity;

public enum MeetingStatus {

    M10_IN_PROGRESS("会议进行中"),
    M20_BEGIN_READY("即将开始"),
    M30_NOT_STARTED("未开始"),
    M50_CANCELED("已取消"),
    M40_FINISHED("已结束");

    private String desc;

    MeetingStatus(String desc) {
        this.desc=desc;
    }
}
