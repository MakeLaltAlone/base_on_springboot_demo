package com.ytf.springboot.demo.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/20 18:36
 * @Description: 消费者
 */
@Component
@RabbitListener(queues = "YTF")   //监听YTF这个队列
public class Consumer2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer2.class);

    @RabbitHandler()
    public void consumer2(Integer message){
        LOGGER.info("receive message:{}",message);
        System.out.println("consumer2-消费消息："+message);
    }

}
