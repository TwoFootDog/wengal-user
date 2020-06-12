package com.project.domain.user.service;

import com.project.domain.user.model.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserAccountService {
    public SingleResult<LoginResult> login(LoginRequest request, HttpServletResponse response);
    public CommonResult logout(HttpServletRequest request, HttpServletResponse response);
    public SingleResult<SignUpResult> signUp(SignUpRequest request) throws Exception;
    public SingleResult<DeleteAccountResult> deleteAccount(Long userId) throws Exception;
    public CommonResult refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception;
//    public <T> SingleResult<T> getSingleResult(T data);
//    void setSuccessResult(CommonResult result);
}
