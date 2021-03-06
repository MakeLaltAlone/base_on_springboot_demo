package com.ytf.springboot.demo.controller;

import com.ytf.springboot.demo.Utils.RedisUtil;
import com.ytf.springboot.demo.Utils.ValidationUtil;
import com.ytf.springboot.demo.annotation.LogAopAnnotation;
import com.ytf.springboot.demo.annotation.ParamCheck;
import com.ytf.springboot.demo.model.JsonBody;
import com.ytf.springboot.demo.model.User;
import com.ytf.springboot.demo.rabbitmq.Producer;
import com.ytf.springboot.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/9
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer;

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

    /**
     * 检验实体参数-通过@Valid和BindingResult中获取
     * @param user
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/testValidator",method = RequestMethod.POST)
    @ResponseBody
    public JsonBody testValidator(@Valid @RequestBody User user, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            String errMsg = ValidationUtil.getSingleErrorMsgFrom(bindingResult);
            return JsonBody.fail(errMsg);
        }

        return JsonBody.success();
    }

    /**
     * 校验实体参数-手动从实体中获取
     * @param user
     * @return
     */
    @RequestMapping(value = "/testValidator1",method = RequestMethod.POST)
    @ResponseBody
    public JsonBody testValidator(@RequestBody User user){

        List<String> validate = ValidationUtil.validate(user);
        if(validate!=null&&validate.size()>0){
            return JsonBody.fail(validate.get(0));   //只返回第一个错误信息
        }

        return JsonBody.success();
    }

    /**
     * 校验实体参数-利用AOP
     * @param user
     * @return
     */
    @ParamCheck
    @RequestMapping(value = "/testValidator2",method = RequestMethod.POST)
    @ResponseBody
    public JsonBody testValidator2(@RequestBody User user){

        System.out.println("校验");

        return JsonBody.success();
    }

    @RequestMapping(value = "/testRedisCache",method = RequestMethod.GET)
    @ResponseBody
    public JsonBody testRedisCache() {
        try {
            List<User> users = userService.queryUserAll();
            return JsonBody.success(users);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return JsonBody.fail(e.getMessage());
        }

    }

    @RequestMapping(value = "/testSession")
    @ResponseBody
    public String uid(HttpSession session){
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }

    @RequestMapping(value = "/testRabbitMQ")
    @ResponseBody
    public String testRabbitMQ(){
        for (int i=1;i<=100;++i){
            producer.sendMessage("YTF",i);
        }

        return "success";
    }

}
