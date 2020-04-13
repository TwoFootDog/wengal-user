package com.project.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ErrorResult handleBusinessException(BusinessException e) {

        return new ErrorResult(e.isSuccess(), e.getCode(), e.getMsg());
    }
}
