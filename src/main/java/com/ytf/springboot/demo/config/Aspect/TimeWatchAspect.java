package com.ytf.springboot.demo.config.Aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @Auther: yangtengfei
 * @Date: 2018/9/10 15:24
 * @Description:
 */
@Aspect
@Component
public class TimeWatchAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(TimeWatchAspect.class);

    @Around("@annotation(com.ytf.springboot.demo.annotation.TimeWatch)")
    public Object timeWatch(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start();  //开始计时
        Object proceed = joinPoint.proceed();   //执行方法
        sw.stop();  //结束计时
        LOGGER.info(joinPoint.getSignature().getName()+" 执行时间为："+sw.getTotalTimeMillis()+"ms");
        return proceed;
    }
}
