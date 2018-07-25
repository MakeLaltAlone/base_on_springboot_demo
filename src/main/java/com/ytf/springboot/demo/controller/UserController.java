package com.ytf.springboot.demo.controller;

import com.ytf.springboot.demo.annotation.LogAopAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/9
 */
@Controller
public class UserController {

    /**
     * 功能描述: 自定义注解方式标识该方法是一个切点
     *
     * @param: [params]
     * @return: java.lang.String
     * @auther: yangtengfei
     * @date: 2018/7/20 9:46
     */
    @RequestMapping(value = "/testAopLog",method = RequestMethod.POST)
    @ResponseBody
    @LogAopAnnotation   //通过自定义注解的方式标识该方法是AOP的一个切点
    public String testAopLog(@RequestBody Map<String,Object> params){

        System.out.println("**********方法执行中***********");
        String name = (String) params.get("name");
        String age = (String) params.get("age");
        System.out.println("**********name***********:"+name);
        System.out.println("**********age***********:"+age);

        return "testAopLog 正常结束";
    }

    @RequestMapping(value = "/testAopLog01",method = RequestMethod.POST)
    @ResponseBody
    public String testAopLog01(@RequestBody Map<String,Object> params){

        System.out.println("**********方法执行中***********");
        String name = (String) params.get("name");
        String age = (String) params.get("age");
        System.out.println("**********name***********:"+name);
        System.out.println("**********age***********:"+age);

        return "testAopLog 正常结束";
    }
}
