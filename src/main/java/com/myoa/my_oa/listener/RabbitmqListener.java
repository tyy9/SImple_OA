package com.myoa.my_oa.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myoa.my_oa.entity.CourseOrder;
import com.myoa.my_oa.service.CourseOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RabbitmqListener {
    @Autowired
    CourseOrderService courseOrderService;
    @RabbitListener(queues = "dl-queue")
    public void dl_listener(String s_courseOrder, Message message, Channel channel) throws IOException {
        JSONObject jsonObject = JSON.parseObject(s_courseOrder);
        CourseOrder courseOrder = jsonObject.toJavaObject(CourseOrder.class);
        if(!courseOrder.getStatus()){
            log.info("发现购买状态仍为未购买状态，ack并删除sql数据");
            courseOrderService.removeById(courseOrder.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }else{
            log.info("发现购买状态为购买状态，ack并删除记录");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
        log.info("死信列表",courseOrder);
    }
}
