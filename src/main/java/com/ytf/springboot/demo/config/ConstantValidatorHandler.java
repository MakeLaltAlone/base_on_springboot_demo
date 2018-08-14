package com.ytf.springboot.demo.config;

import com.ytf.springboot.demo.annotation.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/14 14:59
 * @Description:  数据检验，自定义注解的处理类
 */
public class ConstantValidatorHandler implements ConstraintValidator<Constant,String> {

    private String value;

    @Override
    public void initialize(Constant constant) {
        this.value = constant.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //判断参数是否等于设置的字段值，返回结果
        return constraintValidatorContext.equals(s);
    }
}
