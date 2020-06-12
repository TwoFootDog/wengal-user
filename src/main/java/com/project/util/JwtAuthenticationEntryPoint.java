//package com.project.util;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
///* jwt 인증 진입점. spring security 에서 인증 관련 예외가 발생했을 경우 호출됨 */
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private static final Logger logger = LogManager.getLogger(JwtAuthenticationEntryPoint.class);
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//    }
//}
