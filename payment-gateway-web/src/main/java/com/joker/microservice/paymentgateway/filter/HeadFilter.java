package com.joker.microservice.paymentgateway.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;


/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
@WebFilter(filterName="headerFilter",urlPatterns="/*")
public class HeadFilter implements Filter {

        private static Logger logger = LogManager.getLogger(HeadFilter.class);
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                        String name =  headerNames.nextElement();
                        logger.debug("request header : " + name + " ==> " + request.getHeader(name));
                }
                filterChain.doFilter(servletRequest, servletResponse);
        }

        @Override
        public void destroy() {

        }
}
