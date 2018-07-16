package com.ytf.springboot.demo.Exception;

import org.springframework.http.HttpStatus;

/**
 * @Author:TengFeiYang
 * @Description: 自定义异常
 * @Date:Created in 2018/7/6
 */
public class MyException extends Exception{

    private HttpStatus httpStatus;

    private String exceDescr;   //异常描述

    public MyException(String exceDescr){
        this.exceDescr = exceDescr;
    }

    public MyException(HttpStatus httpStatus, String exceDescr) {
        this.httpStatus = httpStatus;
        this.exceDescr = exceDescr;
    }

    public MyException(String message, HttpStatus httpStatus, String exceDescr) {
        super(message);
        this.httpStatus = httpStatus;
        this.exceDescr = exceDescr;
    }

    public MyException(String message, Throwable cause, HttpStatus httpStatus, String exceDescr) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.exceDescr = exceDescr;
    }

    public MyException(Throwable cause, HttpStatus httpStatus, String exceDescr) {
        super(cause);
        this.httpStatus = httpStatus;
        this.exceDescr = exceDescr;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus, String exceDescr) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
        this.exceDescr = exceDescr;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getExceDescr() {
        return exceDescr;
    }

    public void setExceDescr(String exceDescr) {
        this.exceDescr = exceDescr;
    }
}
