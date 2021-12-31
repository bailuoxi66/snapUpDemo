package com.example.springbootfilter.filterInfo;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/31 10:47 上午
 * @description
 */

@Order(2)
@WebFilter(filterName = "secondFilter", urlPatterns = "/*")
public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("second filter 1");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("before:" + servletRequest);
        System.out.println(request.getRequestURL());
        String uri = request.getRequestURI();
        System.out.println(uri);
        if (uri.contains("Test")){
            System.out.println("过滤。。。");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("after:" + servletResponse);
        System.out.println("second filter 2");
    }

    @Override
    public void destroy() {
    }
}
