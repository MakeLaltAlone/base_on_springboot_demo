package com.ytf.springboot.demo.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/20 18:29
 * @Description: 生产者
 */
@Component
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private AmqpTemplate template;

    public void sendMessage(String queueName, Object message){
        LOGGER.info("send message:{}",message);

        template.convertAndSend(queueName,message);
    }
}
