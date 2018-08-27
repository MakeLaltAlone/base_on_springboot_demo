package com.ytf.springboot.demo.Exception;

import com.ytf.springboot.demo.model.JsonBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:TengFeiYang
 * @Description: 全局异常解析器
 *
 * 首先描述实现的功能：因为在项目中，我们不可否认的会出现异常，而且这些异常并没有进行捕获。经常出现的bug如空指针异常等等。
 * 在之前的项目中，如果我们没有进行任何配置，那么容器会自动打印错误的信息，如果tomcat的404页面，400页面等等
 * @Date:Created in 2018/7/6
 */
@ControllerAdvice    //控制器增强，使@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法应用到所有的@RequestMapping注解的方法
public class GlobalExceptionResolver {

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public JsonBody myException(MyException myException){
        LOGGER.info("全局异常拦截器--自定义异常");
        //这里可写逻辑
        return JsonBody.fail(myException.getExceDescr());
    }

    @ExceptionHandler(value = UndeclaredThrowableException.class)
    @ResponseBody
    public JsonBody checkParamException(UndeclaredThrowableException throwable){
        LOGGER.info("全局异常拦截器--自定义异常");
        //这里可写逻辑
        return JsonBody.fail(throwable.getUndeclaredThrowable().getMessage());
    }

    @ExceptionHandler(value = Exception.class)    //异常处理器，此注解的作用是当出现其定义的异常时进行处理的方法
    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        LOGGER.info("全局异常拦截器--非自定义异常");
        //当然也可以直接返回ModelAndView等类型，然后跳转相应的错误页面，这都根据实际的需要进行使用
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        Map<String,String> map = new HashMap<>();
        map.put("ErrorMassage",e.getMessage());
        view.setAttributesMap(map);

        ModelAndView mav = new ModelAndView();
        int status = response.getStatus();
        mav.setStatus(HttpStatus.valueOf(status));
        mav.setView(view);
        return mav;
    }


//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//
//        ModelAndView mav = new ModelAndView();
//
//        mav.addObject("exception", e);
//
//        mav.addObject("url", req.getRequestURL());
//
//        mav.setViewName(DEFAULT_ERROR_VIEW);
//
//        return mav;
//
//    }
}
