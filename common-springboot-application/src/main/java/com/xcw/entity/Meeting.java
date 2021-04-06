package com.xcw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @class: Meeting
 * @author: ChengweiXing
 * @description: TODO
 **/
@Data
@ApiModel("会议实体")
@TableName("meeting")
public class Meeting {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "直播id")
    private String meetingLiveId;

    @NotBlank(message = "会议室Id不能为空")
    @ApiModelProperty(value = "云会议室")
    private String confNo;

    @NotBlank(message = "会议主题不能为空")
    @ApiModelProperty(value = "会议主题")
    private String title;

    @ApiModelProperty(value = "直播终端")
    @NotNull(message = "直播终端不能为空")
    private String nemoNumber;

    @NotNull(message = "主持人Id不能为空")
    @ApiModelProperty(value = "主持人Id")
    private Long meetingModeratorId;

    @NotBlank(message = "主持人姓名不能为空")
    @ApiModelProperty(value = "主持人")
    private String meetingModerator;

    @ApiModelProperty(value = "会议状态")
    private MeetingStatus meetingStatus;

    @ApiModelProperty(value = "实际是否真正开始了直播")
    private Boolean actualStartLive;

    @NotNull(message = "开启录制不能为空")
    @ApiModelProperty(value = "视频录制")
    private Boolean autoRecording;

    @ApiModelProperty(value = "转储失败后尝试次数")
    private Integer xylinkRetry;

    @ApiModelProperty(value = "全部视频转储成功")
    private Boolean videoTransSuccess;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "会议开始时间不能是过去")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "会议结束时间不能是过去")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "实际的开始时间")
    private Date realStartTime;

    @ApiModelProperty(value = "实际的结束时间")
    private Date realEndTime;

    @ApiModelProperty(value = "会议时长")
    private Long meetingDuration;

    @ApiModelProperty(value = "会议描述")
    private String meetingDescription;

    @ApiModelProperty(value = "直播观看地址")
    private String meetingFlv;

    @ApiModelProperty(value = "直播观看地址")
    private String meetingHls;

    @ApiModelProperty(value = "直播观看地址")
    private String meetingRtmp;

    @ApiModelProperty(value = "直播观看地址")
    private String meetingViewUrl;

    @ApiModelProperty(value = "二维码下载地址")
    private String qrcodeDownloadUrl;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否是主持人")
    private Boolean isModerator;

//    @TableField(exist = false)
//    @ApiModelProperty(value = "会议录像")
//    private List<MeetingVideoDTO> meetingVideoList;

    @TableField(exist = false)
    @ApiModelProperty(value = "实到人数")
    private Integer actualNumber;
}
