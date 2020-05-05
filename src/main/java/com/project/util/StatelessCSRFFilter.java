package com.project.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/* Double Submit Cookie 검증 패턴으로 CSRF 방어로직 구현 */
//@Component
public class StatelessCSRFFilter extends OncePerRequestFilter {
    private static final String HEADER_CSRF_TOKEN = "X-CSRF-TOKEN";
    private static final String COOKIE_CSRF_TOKEN = "CSRF_TOKEN";
    private static final int EXPIRE = 0;
    private final RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("StatelessCSRFFilter>>>>");
        if (requireCsrfProtectionMatcher.matches(request)) {
            logger.info("requireCsrfProtectionMatcher.matches(request)");
            final String csrfHeaderToken = request.getHeader(HEADER_CSRF_TOKEN);
            final Cookie[] cookies = request.getCookies();

            String csrfCookieToken = null;
            if (cookies != null) {
                Cookie cookie  = WebUtils.getCookie(request, COOKIE_CSRF_TOKEN);
                csrfCookieToken = cookie.getValue();
/*                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(CSRF_TOKEN_COOKIE)) {
                        csrfCookieToken = cookie.getValue();
                    }
                }*/
            }
            if (csrfHeaderToken == null || !csrfHeaderToken.equals(csrfCookieToken)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                accessDeniedHandler.handle(request, response, new AccessDeniedException("Missing or non-matching CSRF-token"));
                logger.info("Missing/bad CSRF-TOKEN while CSRF is enabled for request : " + request.getRequestURI());
                return;
            }
        }
        invalidate(response);   // 쿠키 만료 처리
        filterChain.doFilter(request, response);
    }

    /* 쿠키 만료처리 시키는 함수 */
    private void invalidate(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_CSRF_TOKEN, "");
        cookie.setMaxAge(EXPIRE);
        logger.info("invalidate cookie");
        response.addCookie(cookie);
    }

    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        Logger logger = LogManager.getLogger(StatelessCSRFFilter.class);
        private static final String contextRoot = "/user/api/v1";
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
        private final Pattern allowedURI = Pattern.compile(contextRoot+"/login|"+
                                                           contextRoot+"/user");

        @Override
        public boolean matches(HttpServletRequest request) {
            logger.info("request.getMethod() : " + request.getMethod());
            logger.info("request.getRequestURI() : " + request.getRequestURI());

            logger.info(" matches : " + ((!allowedMethods.matcher(request.getMethod()).matches() &&
                    !allowedURI.matcher(request.getRequestURI()).find()) ||
                    ("PUT".equals(request.getMethod()) && request.getRequestURI().contains(contextRoot+"/user"))));
            return (!allowedMethods.matcher(request.getMethod()).matches() &&   // GET/HEAD/TRACE/OPTION 메소드가 아니고
                    !allowedURI.matcher(request.getRequestURI()).find()) ||  // /login과 /user(회원등록)이 아니거나
                    ("PUT".equals(request.getMethod()) && request.getRequestURI().contains(contextRoot+"/user"));
                    // 메소드가 PUT이고 /user(회원정보수정)인 경우 true
        }
    }
}
