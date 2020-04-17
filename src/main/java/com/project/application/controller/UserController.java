package com.project.application.controller;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.model.entity.User;
import com.project.domain.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user")
    public SingleResult<SignUpResult> signUp(@RequestBody SignUpRequest request) throws Exception {
        return userService.signUp(request);
    }

    @DeleteMapping("/user/{userId}")
    public SingleResult<DeleteAccountResult> deleteAccount(@PathVariable Long userId) throws Exception {
        return userService.deleteAccount(userId);
    }

//    @DeleteMapping("/delete/{userId}")
//    public SingleResult2 deleteAccount(@PathVariable Long userId) throws Exception {
//        return userService.deleteAccount2(userId);
//    }

    @GetMapping("/test")
    public String testFunction() {
        logger.info("tesetSuccess");
        return "testSuccess";
    }
}
