package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/user/api/v1/signup").permitAll();
        http.cors();
        http.csrf().disable();
    }
}
