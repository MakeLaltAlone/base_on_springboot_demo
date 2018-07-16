package com.ytf.springboot.demo.config;

import com.ytf.springboot.demo.config.filter.FirstFilter;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/5
 */
@Configuration
public class WebConfiguration {

    @Bean
    public RemoteIpFilter remoteIpFilter(){
        return new RemoteIpFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new FirstFilter());  //添加过滤器
        filterRegistrationBean.addUrlPatterns("/*");  //要拦截的url
        filterRegistrationBean.addInitParameter("paramName","paramValue");   //设置初始化参数，可以在过滤器的init方法代码中取到
        filterRegistrationBean.setName("MyFilter");
        filterRegistrationBean.setOrder(1);   //设置过滤器的优先级
        return filterRegistrationBean;

        //@Bean 和 FilterRegistrationBean 相当于在传统SSM框架中的web.xml中配置以下内容
//        <filter>
//            <filter-name>FirstFilter</filter-name>
//            <filter-class>FirstFilter</filter-class>
//            <init-param>
//            <param-name>encoding</param-name>
//            <param-value>GB2312</param-value>
//            </init-param>
//        </filter>
//
//        <filter-mapping>
//            <filter-name>FirstFilter</filter-name>
//            <url-pattern>/*</url-pattern>
//        </filter-mapping>
    }
}
