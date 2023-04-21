package com.myoa.my_oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author tyy9
 * @since 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Course对象", description="")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "学分")
    private Integer score;

    @ApiModelProperty(value = "上课时间")
    private String times;

    @ApiModelProperty(value = "是否开课")
    private Boolean state;

    @ApiModelProperty(value = "授课老师id")
    private Integer teacherId;

    @ApiModelProperty(value = "课程主分类id")
    private String subjectId;

    @ApiModelProperty(value = "课程子分类id")
    private String subjectParentid;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片")
    private String avatar;

    @ApiModelProperty(value = "购买数量")
    private Long buycount;

    @ApiModelProperty(value = "购买价格")
    private BigDecimal price;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除")
    private Boolean isDeleted;
}
