package com.project.domain.user.service;

import com.project.domain.user.model.dto.*;

public interface UserAccountService {
    public SingleResult<SignUpResult> signUp(SignUpRequest request) throws Exception;
    public SingleResult<DeleteAccountResult> deleteAccount(Long userId) throws Exception;
//    public <T> SingleResult<T> getSingleResult(T data);
//    void setSuccessResult(CommonResult result);
}