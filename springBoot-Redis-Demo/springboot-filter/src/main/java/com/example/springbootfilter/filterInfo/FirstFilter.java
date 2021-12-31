package com.example.springbootfilter.filterInfo;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/31 10:47 上午
 * @description
 */


@Order(1)
@WebFilter(filterName = "firstFilter", urlPatterns = "/*")
public class FirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("first filter 1");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("first filter 2");
    }

    @Override
    public void destroy() {
    }
}
