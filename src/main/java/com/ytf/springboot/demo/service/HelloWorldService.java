package com.ytf.springboot.demo.service;

import com.ytf.springboot.demo.Exception.MyException;
import com.ytf.springboot.demo.Utils.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/7
 */
@Service
public class HelloWorldService {



    /**
     * 除法
     * @param dividend
     * @param divisor
     * @return
     */
    public String div(String dividend, String divisor) throws MyException {
        boolean dividendFlag = NumberUtils.judgeTwoDecimal(dividend);
        boolean divisorFlag = NumberUtils.judgeTwoDecimal(divisor);
        if(!(dividendFlag)){
            throw new MyException("被除数格式错误");
        }
        if(!(divisorFlag)){
            throw new MyException("除数格式错误");
        }
        return new BigDecimal(dividend).divide(new BigDecimal(divisor),2,BigDecimal.ROUND_HALF_DOWN).toString();

    }

}
