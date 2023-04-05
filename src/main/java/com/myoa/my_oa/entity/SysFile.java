package com.myoa.my_oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
 * @since 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysFile对象", description="")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "文件大小(kb)")
    private Long size;

    @ApiModelProperty(value = "下载链接")
    private String url;

    @ApiModelProperty(value = "文件md5")
    private String md5;

    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "是否禁用链接")
    private Boolean enable;


}
