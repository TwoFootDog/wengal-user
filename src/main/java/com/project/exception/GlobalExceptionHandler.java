package com.project.exception;

import com.project.domain.user.model.dto.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /* Service 에러 처리 Handler */
    @ExceptionHandler(value = ServiceException.class)
    public ExceptionResult handleBusinessException(ServiceException e) {
        ExceptionResult exceptionResult = new ExceptionResult();
        setFailResult(exceptionResult, e);
//        exceptionResult.setE(e.getE());
        return exceptionResult;
    }

    @ExceptionHandler(value = Exception.class)
    public ExceptionResult handleFilterException(int status) {
        ExceptionResult exceptionResult = new ExceptionResult();
        setFailResult(exceptionResult, status);
        return exceptionResult;
    }

    /* 성공 시 메시지 및 코드 셋팅하는 함수 */
    private void setFailResult(CommonResult result, ServiceException e) {
        result.setCode(e.getCode());
        result.setMsg(e.getMsg());
        result.setSuccess(e.isSuccess());
    }

    /* 성공 시 메시지 및 코드 셋팅하는 함수 */
    private void setFailResult(CommonResult result, int status) {
        result.setCode(status);
        result.setMsg("aa");
        result.setSuccess(false);
    }
}
