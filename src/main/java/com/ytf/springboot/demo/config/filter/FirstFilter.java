package com.ytf.springboot.demo.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/5
 */
public class FirstFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLogger(FirstFilter.class);

    private FilterConfig filterConfig;

    /**
     * init 方法在 Filter 生命周期中仅被执行一次
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String paramName = filterConfig.getInitParameter("paramName");
    }

    /**
     * 当一个 Filter 对象能够拦截访问请求时，Servlet 容器将调用 Filter 对象的 doFilter 方法。
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("FirstFilter 开始...");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        LOGGER.info("FirstFilter uri:{}",uri);

        System.out.println("权限校验成功");

        LOGGER.info("FirstFilter 结束...   开始调用目标方法...");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /**
     * 该方法在 Web 容器卸载 Filter 对象之前被调用，也仅执行一次。可以完成与 init 方法相反的功能，释放被该 Filter 对象打开的资源，例如：关闭数据库连接和 IO 流
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
