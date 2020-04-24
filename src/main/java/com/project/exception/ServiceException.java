package com.project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    private boolean success;    // 응답 성공여부(true/false)
    private int code;           // 응답 코드 번호 (>= 0 : 정상, < 0 비정상)
    private String msg;         // 응답 메시지
    private Exception e;

    public ServiceException(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}
