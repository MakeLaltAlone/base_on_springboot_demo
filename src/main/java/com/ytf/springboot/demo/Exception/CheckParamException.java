package com.ytf.springboot.demo.Exception;

/**
 * @Auther: yangtengfei
 * @Date: 2018/8/22 11:19
 * @Description: 自定义校验参数异常
 */
public class CheckParamException extends Exception {

    public CheckParamException() {
    }

    public CheckParamException(String message) {
        super(message);
    }

    public CheckParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckParamException(Throwable cause) {
        super(cause);
    }

    public CheckParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
