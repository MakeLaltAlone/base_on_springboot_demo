package com.ytf.springboot.demo.config.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author:TengFeiYang
 * @Description: 日志切面
 * 【注解：Around . 环绕前】方法环绕start.....
 * 【注解：Before】浏览器输入的网址URL:http://127.0.0.1:8080/testAopLog
 * 【注解：Before】HTTP_METHOD:POST
 * 【注解：Before】IP:127.0.0.1
 * 【注解：Before】执行业务的方法名:com.ytf.springboot.demo.controller.UserController.testAopLog
 * 【注解：Before】执行业务的参数:[{name=ytf, age=25}]
 * **********方法执行中***********
 * **********name***********:ytf
 * **********age***********:25
 * 【注解：Around. 环绕后】方法环绕proceed，结果是 :testAopLog 正常结束
 * 【注解：After】方法最后执行.....
 * 【注解：AfterReturning】这个会在切面最后的最后打印，方法的返回值 : testAopLog 正常结束
 * @Date:Created in 2018/7/16
 */
@Aspect
@Component
public class LogAspect {

    // 用自定义注解的方式切入
    @Pointcut("@annotation(com.ytf.springboot.demo.annotation.LogAopAnnotation)")
    public void logPointCutByAnnotation(){};

    // 用表达式的方式切入
    @Pointcut("execution(* com.ytf.springboot.demo.controller.*.test*(..))")  //所有以test开头命名的方法
    public void logPointCutByExcution(){};


    @Before(value = "logPointCutByAnnotation()||logPointCutByExcution()")  //加LogAopAnnotation注解的方法和满足表达式的方法都会执行
    public void doBefore(JoinPoint joinPoint){  //通过JoinPoint获取切点的内容

        //通过RequestContextHolder获取请求信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        System.out.println("【注解：Before】浏览器输入的网址URL:"+request.getRequestURL().toString());
        System.out.println("【注解：Before】HTTP_METHOD:"+request.getMethod());
        System.out.println("【注解：Before】IP:"+request.getRemoteAddr());

        System.out.println("【注解：Before】执行业务的方法名:"+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        System.out.println("【注解：Before】执行业务的参数:"+Arrays.toString(joinPoint.getArgs()));
    }


    @AfterReturning(returning = "ret", pointcut = "logPointCutByAnnotation()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("【注解：AfterReturning】这个会在切面最后的最后打印，方法的返回值 : " + ret);
    }

    //后置异常通知
    @AfterThrowing("logPointCutByAnnotation()")
    public void throwss(JoinPoint jp){
        System.out.println("【注解：AfterThrowing】方法异常时执行.....");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("logPointCutByAnnotation()")
    public void after(JoinPoint jp){
        System.out.println("【注解：After】方法最后执行.....");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("logPointCutByAnnotation()")
    public Object arround(ProceedingJoinPoint pjp) {
        System.out.println("【注解：Around . 环绕前】方法环绕start.....");
        try {
            //pjp.proceed()的返回结果就是对应切点方法的返回结果
            Object o =  pjp.proceed();//如果不执行这句，会不执行切面的Before方法及controller的业务方法

            //可以通过 instanceof 判断是否是哪个实体，然后进行强转
            System.out.println("【注解：Around. 环绕后】方法环绕proceed，结果是 :" + o);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
