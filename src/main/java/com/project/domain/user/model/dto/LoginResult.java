package com.project.domain.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResult {
    private String userId;
    private String email;
    private String nickName;
    // private String token; refresh token은 추후 사용
}
