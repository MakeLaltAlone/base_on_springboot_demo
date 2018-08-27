package com.ytf.springboot.demo.config.Aspect;

import com.ytf.springboot.demo.Exception.CheckParamException;
import com.ytf.springboot.demo.annotation.ParamCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/22 11:27
 * @Description:  校验参数切面
 */
@Aspect
@Component
public class ParamCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamCheckAspect.class);

    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    @Around(value = "@annotation(com.ytf.springboot.demo.annotation.ParamCheck)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            ParamCheck paramCheckAnnotation = method.getAnnotation(ParamCheck.class);
            boolean check = paramCheckAnnotation.isCheck();
            if (paramCheckAnnotation == null || !check){
                return joinPoint.proceed();
            }

            Object[] args = joinPoint.getArgs();
            for (Object object : args){
                Set<ConstraintViolation<Object>> validate = validator.validate(object, Default.class);
                if (validate != null && !validate.isEmpty()){
                    for (ConstraintViolation constraintViolation : validate){
                        LOGGER.error(constraintViolation.getMessage());
                        throw new CheckParamException(constraintViolation.getMessage());    //利用全局异常处理类捕获
                    }
                }
            }
            return joinPoint.proceed();
    }
}
