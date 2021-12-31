package com.example.springbootuserfilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author ：luoyu
 * @version ：1.0
 * @date ： 2021/12/31 3:08 下午
 * @description
 */

@Configuration
public class AdminFilterConfig {

    @Bean
    FilterRegistrationBean testFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AdminFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/admin/*"));
        return filterRegistrationBean;
    }

    static class AdminFilter implements javax.servlet.Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "admin filter error.");
        }

        @Override
        public void destroy() {

        }

    }

}