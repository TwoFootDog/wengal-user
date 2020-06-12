package com.project.application.controller;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.service.UserAccountService;
import com.project.util.CookieUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserAccountService userAccountService;
    @Autowired
    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/login")
    public SingleResult<LoginResult> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return userAccountService.login(request, response);
    }

    @PostMapping("/logout")
    public CommonResult logout(HttpServletRequest request, HttpServletResponse response) {
        return userAccountService.logout(request, response);
    }

    @PostMapping("/user")
    public SingleResult<SignUpResult> signUp(@RequestBody SignUpRequest request) throws Exception {
        return userAccountService.signUp(request);
    }

    @DeleteMapping("/user/{userId}")
    public SingleResult<DeleteAccountResult> deleteAccount(@PathVariable Long userId) throws Exception {
        return userAccountService.deleteAccount(userId);
    }

    @GetMapping("/refresh")
    public CommonResult checkRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userAccountService.refreshToken(request, response);
    }

    @GetMapping("/test")
    public String testFunction() {
        logger.info("tesetSuccess");
        return "testSuccess";
    }
}
