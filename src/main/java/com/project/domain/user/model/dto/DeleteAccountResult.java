package com.project.domain.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/* 회원 탈회 결과파일(data) */
@Data
@AllArgsConstructor
public class DeleteAccountResult {
    private Long userId;
}
