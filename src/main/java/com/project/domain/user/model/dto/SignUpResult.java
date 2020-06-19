package com.project.domain.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 회원 등록 결과파일(data) */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResult {
    private String userId;
    private String email;
    private String nickname;
}
