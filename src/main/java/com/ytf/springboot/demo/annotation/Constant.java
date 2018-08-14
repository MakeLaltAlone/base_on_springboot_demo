package com.ytf.springboot.demo.annotation;

import com.ytf.springboot.demo.config.ConstantValidatorHandler;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 参数只能为特定的值
 */
@Documented
@Constraint(validatedBy = {ConstantValidatorHandler.class })   //指定注解的处理类
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Constant {

    String message() default "{constraint.default.const.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

}
