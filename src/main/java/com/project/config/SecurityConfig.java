package com.project.config;

import com.project.domain.user.service.UserAuthorityService;
import com.project.util.JwtAuthenticationFilter;
import com.project.util.JwtTokenProvider;
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

    private UserAuthorityService userAuthorityService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(UserAuthorityService userAuthorityService,
                          JwtTokenProvider jwtTokenProvider) {
        this.userAuthorityService = userAuthorityService;
        this.jwtTokenProvider = jwtTokenProvider;
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
//                .anyRequest().authenticated()   // 인증된 사용자인지 확인
                .and()
//                    .addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // Jwt 를 이용한 인증이므로 세션은 생성 안함
    }



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
