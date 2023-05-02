    package com.myoa.my_oa.config;

    import org.springframework.amqp.core.*;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class Rabbitconfig {
        @Bean("Dl_exchange")
        public Exchange dlExchange(){
            //创建一个新的死信交换机
            return ExchangeBuilder.directExchange("dlx.direct").build();
        }

        @Bean("Dl_Queue")   //创建一个新的死信队列
        public Queue dlQueue(){
            return QueueBuilder
                    .nonDurable("dl-queue")
                    .build();
        }

        @Bean("dlBinding")   //死信交换机和死信队列进绑定
        public Binding dlBinding(@Qualifier("Dl_exchange") Exchange exchange,
                                 @Qualifier("Dl_Queue") Queue queue){
            return BindingBuilder
                    .bind(queue)
                    .to(exchange)
                    .with("dl-key")
                    .noargs();
        }

        @Bean("userExchange")  //定义交换机Bean，可以很多个
        public Exchange exchange(){
            return ExchangeBuilder.directExchange("amq.direct").build();
        }



        @Bean("binding")
        public Binding binding(@Qualifier("userExchange") Exchange exchange,
                               @Qualifier("userQueue") Queue queue){
            //将我们刚刚定义的交换机和队列进行绑定
            return BindingBuilder
                    .bind(queue)   //绑定队列
                    .to(exchange)  //到交换机
                    .with("user-key")   //使用自定义的routingKey
                    .noargs();
        }

        @Bean("userQueue")
        public Queue queue(){
            return QueueBuilder
                    .nonDurable("user-queue")
                    .deadLetterExchange("dlx.direct")   //指定死信交换机
                    .deadLetterRoutingKey("dl-key")
                    .ttl(1000*60*5)
                    .maxLength(1)
                    .build();
        }
    }
