package com.project.domain.user.service;

import com.project.domain.user.model.dto.*;

import javax.servlet.http.HttpSession;

public interface UserAccountService {
    public SingleResult<LoginResult> login(LoginRequest request, HttpSession httpSession);
    public SingleResult<SignUpResult> signUp(SignUpRequest request) throws Exception;
    public SingleResult<DeleteAccountResult> deleteAccount(Long userId) throws Exception;
//    public <T> SingleResult<T> getSingleResult(T data);
//    void setSuccessResult(CommonResult result);
}
