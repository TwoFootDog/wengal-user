package com.project.domain.user.model.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
}
