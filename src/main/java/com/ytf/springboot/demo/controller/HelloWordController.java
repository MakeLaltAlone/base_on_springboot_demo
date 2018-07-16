package com.ytf.springboot.demo.controller;

import com.ytf.springboot.demo.Exception.MyException;
import com.ytf.springboot.demo.config.ParameterConfig;
import com.ytf.springboot.demo.model.JsonBody;
import com.ytf.springboot.demo.model.User;
import com.ytf.springboot.demo.service.HelloWorldService;
import com.ytf.springboot.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/6/13
 */
@Controller
//@RestController   该注解表示类中的所有方法都会以Json的格式返回
public class HelloWordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWordController.class);

    @Value("${com.ytf.title}")
    private String title;

    @Autowired
    private ParameterConfig parameterConfig;

    @Autowired
    private HelloWorldService helloWorldService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/sayHello")
    public String sayHello(){
        return "Hello World...";
    }


    // http://localhost:8080/getUser?name=小飞&age=25
    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    @ResponseBody     //该注解表示该方法以Json格式返回
    public User getUser(@RequestParam("name")String name, @RequestParam("age")Integer age){
        LOGGER.info("HelloWordController.getUser params:name-{},age-{}",name,age);
        System.out.println("从配置文件读取参数值："+parameterConfig.getTitle());
        User user = new User();
        user.setName(name);
        user.setAge(age);
        LOGGER.info("HelloWordController.getUser result:{}",user.toString());
        return user;
    }

    // http://localhost:8080/getUser01?name=小飞&age=25
    @RequestMapping(value = "/getUser01",method = RequestMethod.GET)
    @ResponseBody     //该注解表示该方法以Json格式返回
    public User getUser01(@RequestParam Map<String,Object> params){
        LOGGER.info("HelloWordController.getUser params:{}",params);
        String name = (String) params.get("name");
        String ageStr = (String) params.get("age");
        User user = new User();
        user.setName(name);
        if(StringUtils.isNotEmpty(ageStr)){
            user.setAge(Integer.valueOf(ageStr));
        }
        LOGGER.info("HelloWordController.getUser result:{}",user.toString());
        return user;
    }

    @RequestMapping(value = "/testSendErrorLog",method = RequestMethod.GET)
    @ResponseBody     //该注解表示该方法以Json格式返回
    public User testSendErrorLog(@RequestParam Map<String,Object> params){
        LOGGER.info("HelloWordController.getUser params:{}",params);
        User user = null;
        try {
            String name = (String) params.get("name");
            String ageStr = (String) params.get("age");
            user = new User();
            user.setName(name);
            if(StringUtils.isNotEmpty(ageStr)){
                user.setAge(Integer.valueOf(ageStr));
            }
            int a = 10/0;   //Controller层产生的异常会被GlobalExceptionResolver捕获到并处理，不会被下面的catch捕获到
        } catch (NumberFormatException e) {
            LOGGER.error("HelloWordController.getUser error",e);
            return null;
        }
        LOGGER.info("HelloWordController.getUser result:{}",user.toString());
        return user;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody     //该注解表示该方法以Json格式返回
    public JsonBody addUser(User user){
        LOGGER.info("HelloWordController.addUser params:{}",user.toString());
        try {
            userService.addUser(user);
        } catch (Exception e) {
            LOGGER.error("HelloWordController.getUser error",e);
            return JsonBody.fail(e.getMessage());
        }
        LOGGER.info("HelloWordController.getUser result:{}",user.toString());
        return JsonBody.success("success");
    }

    /**
     * 除法
     * @param params
     * @return
     */
    @RequestMapping(value = "/divided",method = RequestMethod.GET)
    @ResponseBody
    public String divided(@RequestParam Map<String,Object> params){
        LOGGER.info("HelloWordController.divided params:{}",params);
        String div = null;
        try {
            String dividend = (String) params.get("dividend");
            String divisor = (String) params.get("divisor");
            div = helloWorldService.div(dividend, divisor);
//            int a = 10/0;   //Controller层产生的异常会被GlobalExceptionResolver捕获到并处理，不会被下面的catch捕获到
        }  catch (MyException myExc) {
            LOGGER.error("HelloWordController.divided errorMessage",myExc.getExceDescr(),myExc);
            return div;
        } catch (Exception e) {
            LOGGER.error("HelloWordController.divided errorMessage",e.getMessage(),e);
            return div;
        }
        LOGGER.info("HelloWordController.divided result:{}",div);
        return div;
    }
}
