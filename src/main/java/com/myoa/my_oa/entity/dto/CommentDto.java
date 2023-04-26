package com.myoa.my_oa.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CommentDto对象", description="")
public class CommentDto {
    @ApiModelProperty(value = "评论id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论用户名字")
    private String username;

    @ApiModelProperty(value = "评论用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "评论用户id")
    private Integer userId;

    @ApiModelProperty(value = "回复用户的名字")
    private String replyuser_username;

    @ApiModelProperty(value = "回复用户的评论内容")
    private String reply_content;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
}
