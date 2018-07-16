package com.ytf.springboot.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:TengFeiYang
 * @Description: 自定义注解，用于Aop的记录日志
 * @Date:Created in 2018/7/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface LogAopAnnotation {
    String value() default "";
}
