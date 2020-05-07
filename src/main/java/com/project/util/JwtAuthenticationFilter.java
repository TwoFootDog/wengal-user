package com.project.util;


import com.project.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Slf4j
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {    // Jwt가 유효한 토큰인지 인증하기 위한 Fillter

    Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
//    @Value("${jwt.accessToken.name}")
    private static final String ACCESS_TOKEN_NAME =  "X-AUTH-TOKEN"; // X-AUTH-TOKEN
    private static final String REFRESH_TOKEN_NAME = "X-REF-TOKEN";

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("JWTAuthenticationFilter doFilter");
        logger.info("access token name : " + ACCESS_TOKEN_NAME);
        String accessToken  = jwtTokenUtil.resolveToken((HttpServletRequest)request, ACCESS_TOKEN_NAME, "N");
        String refreshToken  = jwtTokenUtil.resolveToken((HttpServletRequest)request, REFRESH_TOKEN_NAME, "Y");
        boolean isAccessTokenValid = jwtTokenUtil.validateToken(accessToken);
        if (accessToken != null && isAccessTokenValid) {   // access token 이 유입되었고 유효한 경우
            Authentication authentication = jwtTokenUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if(accessToken != null && !isAccessTokenValid) {

        } else if(accessToken == null && ) {

        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.info("token is invalid>>>>>>> : " + accessToken);
        }

        logger.info("doFilter>>>>>>>>>>>>>>>>>>>>>>" + accessToken);
        logger.info("ServletRequest>>>>>>>>>>>>>>>>>>>>>>" + ((HttpServletRequest) request).getHeader("X-AUTH-TOKEN"));

        chain.doFilter(request, response);  // Filter를 FilterChain에 등록

    }
/*
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("JWTAuthenticationFilter doFilter");
        logger.info("access token name : " + ACCESS_TOKEN_NAME);
        String accessToken  = jwtTokenUtil.resolveToken((HttpServletRequest)request, ACCESS_TOKEN_NAME);
        boolean accessTokenValid = jwtTokenUtil.validateToken(accessToken);
        if (accessToken != null && accessTokenValid) {   // access token 이 유입되었고 유효한 경우
                Authentication authentication = jwtTokenUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("token is invalid>>>>>>> : " + accessToken);
        }

        logger.info("doFilter>>>>>>>>>>>>>>>>>>>>>>" + accessToken);
        logger.info("ServletRequest>>>>>>>>>>>>>>>>>>>>>>" + ((HttpServletRequest) request).getHeader("X-AUTH-TOKEN"));

        chain.doFilter(request, response);  // Filter를 FilterChain에 등록
    }*/
}
