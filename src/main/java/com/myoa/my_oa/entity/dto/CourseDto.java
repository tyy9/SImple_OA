package com.myoa.my_oa.entity.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Course对象", description="")
public class CourseDto implements Serializable {

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

    @ApiModelProperty(value = "课程分类id")
    private Integer subjectId;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片")
    private String avatr;


}