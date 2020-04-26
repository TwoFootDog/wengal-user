package com.project.util;


import com.project.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {    // Jwt가 유효한 토큰인지 인증하기 위한 Fillter

//    @Value("jwt.cookieName")
    private String jwtCookieName = "X-AUTH-TOKEN";

    Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token  = jwtTokenProvider.resolveToken((HttpServletRequest)request, jwtCookieName);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("token dofilter >>>>>>>");
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.info("token is invalid>>>>>>> : " + token);
        }
        logger.info("doFilter>>>>>>>>>>>>>>>>>>>>>>" + token);
        logger.info("ServletRequest>>>>>>>>>>>>>>>>>>>>>>" + ((HttpServletRequest) request).getHeader("X-AUTH-TOKEN"));

        chain.doFilter(request, response);  // Filter를 FilterChain에 등록
    }
}
