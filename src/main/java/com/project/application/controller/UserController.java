package com.project.application.controller;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.service.UserAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserAccountService userAccountService;
    @Autowired
    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }


    @PostMapping("/user")
    public SingleResult<SignUpResult> signUp(@RequestBody SignUpRequest request) throws Exception {
        return userAccountService.signUp(request);
    }

    @DeleteMapping("/user/{userId}")
    public SingleResult<DeleteAccountResult> deleteAccount(@PathVariable Long userId) throws Exception {
        return userAccountService.deleteAccount(userId);
    }

    @GetMapping("/test")
    public String testFunction() {
        logger.info("tesetSuccess");
        return "testSuccess";
    }
}
