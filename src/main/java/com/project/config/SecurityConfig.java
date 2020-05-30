package com.project.config;

import com.project.domain.user.service.UserAuthorityService;
import com.project.util.JwtAuthenticationEntryPoint;
import com.project.util.JwtAuthenticationFilter;
import com.project.util.JwtTokenUtil;
import com.project.util.StatelessCSRFFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LogManager.getLogger(UserAuthorityService.class);

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UserAuthorityService userAuthorityService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public SecurityConfig(UserAuthorityService userAuthorityService,
                          JwtTokenUtil jwtTokenUtil) {
        this.userAuthorityService = userAuthorityService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/resources/**",
                        "/css/**",
                        "/script/**",
                        "image/**",
                        "fonts/**",
                        "lib/**",
                        "/user/api/v1/swagger-ui.html",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/swagger/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "configuration/security");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user","/login").permitAll()
                .antMatchers("/test").authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
//                .addFilterBefore(new StatelessCSRFFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManagerBean(), jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
        http.cors().disable();
        http.csrf().disable();
/*        http.csrf().ignoringAntMatchers("/login").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
        .addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);*/


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // Jwt 를 이용한 인증이므로 세션은 생성 안함
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    /*private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {

                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                logger.info("CsrfToken1 : " + csrf.getToken());
                logger.info("referer header : " + request.getHeader("REFERER"));
//                logger.info("cookie : " + WebUtils.getCookie(request, "XSRF-TOKEN").getValue());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    if (cookie != null) {
                        logger.info("Cookie : " + cookie.getValue());
                    }
                    if (request.getHeader("X-XSRF-TOKEN") != null) {
                        logger.info("header : " + request.getHeader("X-XSRF-TOKEN"));
                    }
                    String token = csrf.getToken();
                    logger.info("CsrfToken2 : " + token);
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {

                        // Token is being added to the XSRF-TOKEN cookie.
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/user/api/v1");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN1");
        return repository;
    }*/



    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* UserDetailsService 를 이용한 인증처리 */
        auth.userDetailsService(userAuthorityService).passwordEncoder(bcryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* Spring Security에서 사용되는 인증객체를 Bean으로 등록할 때 사용*/
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
