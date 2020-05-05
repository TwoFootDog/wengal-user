package com.project.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class CookieUtil {
    private static final Logger logger = LogManager.getLogger(CookieUtil.class);
    /* 쿠키 생성 */
    public static void create(HttpServletResponse response, String name, String value, Boolean secure, Integer maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);   // secure=true => work on HTTPS only
        cookie.setHttpOnly(true);   // invisible to Javascript
        cookie.setMaxAge(maxAge);   // maxAge=0 : expire cookie now.   maxAge<0: expire cookiie on browser exit
        cookie.setDomain(domain);   // visible to domain only.
        cookie.setPath("/");        // visible all path
        response.addCookie(cookie);
    }
    /* 쿠키 삭제 */
    public static void clear(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setDomain("");
        response.addCookie(cookie);
    }
    /* 쿠키 조회 */
    public static String getValue(HttpServletRequest request, String name) {
        logger.info("getvalue name : " + name);
        Cookie cookie = WebUtils.getCookie(request, name);
        logger.info("getvalue cookie : " + cookie);
        return cookie != null ? cookie.getValue() : null;
    }
}
