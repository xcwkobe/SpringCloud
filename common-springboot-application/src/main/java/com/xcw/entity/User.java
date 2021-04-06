package com.xcw.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @class: User
 * @author: ChengweiXing
 * @description: TODO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户实体")
public class User {

    @ApiModelProperty("用户id")
    private int id;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户密码")
    private String passwd;

    @ApiModelProperty("用户邮箱")
    private String email;
}
