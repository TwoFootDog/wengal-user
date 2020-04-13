package com.project.domain.user.service;

import com.project.domain.user.model.dto.SignUpRequest;
import com.project.domain.user.model.dto.SingleResult;
import com.project.domain.user.model.entity.User;

public interface UserService {
    public SingleResult<User> signUp(SignUpRequest request) throws Exception;
//    public <T> SingleResult<T> getSingleResult(T data);
//    void setSuccessResult(CommonResult result);
}
