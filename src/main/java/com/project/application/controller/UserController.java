package com.project.application.controller;

import com.project.domain.user.model.dto.SignUpRequest;
import com.project.domain.user.model.dto.SingleResult;
import com.project.domain.user.model.entity.User;
import com.project.domain.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public SingleResult<User> signUp(@RequestBody SignUpRequest request) throws Exception {
        return userService.signUp(request);
    }

    @GetMapping("/test")
    public String testFunction() {
        logger.info("tesetSuccess");
        return "testSuccess";
    }
}
