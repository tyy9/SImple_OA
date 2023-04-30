package com.myoa.my_oa.entity.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="Order_time对象", description="")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order_time {
    @ApiModelProperty(value = "订单id")
    private  Long id;
    @ApiModelProperty(value = "剩余时间")
    private  Long time;
}
