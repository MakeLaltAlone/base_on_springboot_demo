package com.ytf.springboot.demo.config.BaseBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: yangtengfei
 * @Date: 2018/7/20 10:10
 * @Description:  UncaughtExceptionHandler是为了捕获没有被捕获的异常，包括运行时异常，执行错误(内存溢出等)，子线程抛出的异常等
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCrashHandler.class);


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //当程序发生未捕获异常时会执行该方法
        LOGGER.error("UncaughtExceptionHandler 捕获到异常。。。");
    }
}
