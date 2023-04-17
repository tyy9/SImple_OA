package com.myoa.my_oa.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.myoa.my_oa.entity.Subject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Subject总和对象", description="课程科目")
public class SubjectDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程类别ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "类别名称")
    private String title;

    @ApiModelProperty(value = "子菜单列表")
    private List<Subject> subjects_children;


}

