package com.xcw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @class: MeetingParticipant
 * @author: ChengweiXing
 * @description: TODO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("参会人实体类")
@TableName("meeting_participant")
public class MeetingParticipant {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "直播Id")
    private Long meetingId;

    @NotNull(message = "参会人账户Id不能为空")
    @ApiModelProperty(value = "账户Id")
    private Long accountId;

    @NotBlank(message = "参会人姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "学校Id")
    private Long schoolId;

    @ApiModelProperty(value = "所在学校")
    private String school;

    @ApiModelProperty(value = "身份")
    private String title;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "是否缺席")
    private MeetingParticipantStatus participantStatus;

    @ApiModelProperty(value = "入会时间")
    private Date joinTime;

    @ApiModelProperty(value = "退会时间")
    private Date leaveTime;

    @ApiModelProperty(value = "会议时长")
    private Long joinDuration;

}
