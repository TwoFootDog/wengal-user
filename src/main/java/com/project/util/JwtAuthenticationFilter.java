package com.project.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {    // Jwt가 유효한 토큰인지 인증하기 위한 Fillter

    private static final String JWT_COOKIE_NAME = "X-AUTH-TOKEN";

    Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token  = jwtTokenUtil.resolveToken((HttpServletRequest)request, JWT_COOKIE_NAME);
        if (token != null && jwtTokenUtil.validateToken(token)) {
                Authentication authentication = jwtTokenUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.info("token is invalid>>>>>>> : " + token);
        }

        logger.info("doFilter>>>>>>>>>>>>>>>>>>>>>>" + token);
        logger.info("ServletRequest>>>>>>>>>>>>>>>>>>>>>>" + ((HttpServletRequest) request).getHeader("X-AUTH-TOKEN"));

        chain.doFilter(request, response);  // Filter를 FilterChain에 등록
    }
}
