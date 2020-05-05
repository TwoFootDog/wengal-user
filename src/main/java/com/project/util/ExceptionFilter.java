package com.project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.ExceptionResult;
import com.project.exception.GlobalExceptionHandler;
import com.project.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.globalExceptionHandler = ctx.getBean(GlobalExceptionHandler.class);
        logger.info("ExceptionFilter^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        logger.info("ExceptionFilter  doFilter^^^^^^^^^^^^^^^^^^^^^^^^^");


            chain.doFilter(request, response);

    }


    @Override
    public void destroy() {

    }
}
