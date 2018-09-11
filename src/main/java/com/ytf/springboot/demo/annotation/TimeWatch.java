package com.ytf.springboot.demo.annotation;

import java.lang.annotation.*;

/**
 * @Auther: yangtengfei
 * @Date: 2018/9/10 15:20
 * @Description: 在需要获取方法执行时间的方法上可以直接加该注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeWatch {
}
