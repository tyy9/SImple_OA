package com.myoa.my_oa.entity.dto;


import com.baomidou.mybatisplus.annotation.*;
import com.myoa.my_oa.entity.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserCommentDto对象", description="")
public class UserCommentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @TableField(fill= FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "介绍")
    private String description;

    @ApiModelProperty(value = "账号激活状态")
    private Boolean status;

    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "评论列表")
    private List<CommentDto> children;




}