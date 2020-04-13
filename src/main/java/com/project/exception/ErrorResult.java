package com.project.exception;

import com.project.domain.user.model.dto.CommonResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class ErrorResult extends CommonResult {
    public ErrorResult(boolean success, int code, String msg) {
        super(success, code, msg);
    }
}
