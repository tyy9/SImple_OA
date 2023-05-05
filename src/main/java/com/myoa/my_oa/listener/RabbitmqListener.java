package com.myoa.my_oa.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.CourseOrder;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.service.CourseOrderService;
import com.myoa.my_oa.service.CourseService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RabbitmqListener {
    @Autowired
    CourseOrderService courseOrderService;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CourseService courseService;

    @RabbitListener(queues = "user-queue")
    public void user_listener(String s_courseOrder, Message message, Channel channel) throws IOException {
        JSONObject jsonObject = JSON.parseObject(s_courseOrder);
        CourseOrder tmp_courseOrder = jsonObject.toJavaObject(CourseOrder.class);
        //从sql里找数据对比
        CourseOrder courseOrder = courseOrderService.getById(tmp_courseOrder);
        if(courseOrder==null){
            log.info("已经取消订单，ack并删除redis数据");
            redisTemplate.delete("order_detail"+tmp_courseOrder.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
        else if(!courseOrder.getStatus()){
            log.info("订单:"+courseOrder.getId());
            log.info("发现购买状态仍为未购买状态，重新放入队列");
            log.info("courseOrder=>"+courseOrder.getStatus());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }else{
            log.info("发现购买状态为购买状态或已经取消订单，ack");
            log.info("courseOrder=>"+courseOrder.getStatus());
            redisTemplate.delete("order_detail"+courseOrder.getId());
            courseOrder.setTime(false);
            courseOrderService.updateById(courseOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        }
        log.info("用户队列",courseOrder);
    }
    @RabbitListener(queues = "dl-queue")
    public void dl_listener(String s_courseOrder, Message message, Channel channel) throws IOException{
        JSONObject jsonObject = JSON.parseObject(s_courseOrder);
        CourseOrder tmp_courseOrder = jsonObject.toJavaObject(CourseOrder.class);
        CourseOrder courseOrder = courseOrderService.getById(tmp_courseOrder);
        log.info("死信列表",courseOrder);
        if(courseOrder==null){
            log.info("已经取消订单，ack并删除redis数据");
            redisTemplate.delete("order_detail"+tmp_courseOrder.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
        else if(!courseOrder.getStatus()){
            log.info("courseOrder=>"+courseOrder.getStatus());
            log.info("发现购买状态仍为未购买状态，ack并删除sql数据和redis数据");
            redisTemplate.delete("order_detail"+courseOrder.getId());
            Course course = courseService.getById(courseOrder.getCourseId());
            Long r = course.getBuycount();
            if(r==0){
                course.setBuycount(0L);
                courseService.updateById(course);
            }else{
                course.setBuycount(r-1);
                courseService.updateById(course);
            }
            courseOrderService.removeById(courseOrder.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            throw new CustomerException(20000,"你有订单在限定时间内仍未付款自动取消或订单处理列表已满时仍未付款");
        }else{
            log.info("发现购买状态为购买状态或已经取消订单，ack并删除redis数据");
            redisTemplate.delete("order_detail"+courseOrder.getId());
            courseOrder.setTime(false);
            courseOrderService.updateById(courseOrder);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }

    }
}
