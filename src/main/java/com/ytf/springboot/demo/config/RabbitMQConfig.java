package com.ytf.springboot.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/20 18:07
 * @Description:
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue(){
        return new Queue("YTF");    //创建一个消息队列
    }
}
