package com.ytf.springboot.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/5
 */
@Component
@PropertySource("classpath:parameter.properties")
@ConfigurationProperties(prefix = "com.ytf")
public class ParameterConfig {

    private String title;

    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
