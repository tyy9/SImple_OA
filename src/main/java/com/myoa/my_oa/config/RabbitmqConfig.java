package com.myoa.my_oa.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;

@Configuration
public class RabbitmqConfig {
    //创建
    @Bean("userExchange")
    public Exchange exchange(){
            return ExchangeBuilder
                    .directExchange("user-Exchange")
                    .build();
    }
}
