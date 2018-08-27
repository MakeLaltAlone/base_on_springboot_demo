package com.ytf.springboot.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/22 11:21
 * @Description: 校验参数的注解  用在方法上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCheck {

    /**
     * 是否校验，默认校验
     * @return
     */
    boolean isCheck() default true;
}
