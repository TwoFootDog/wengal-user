package com.project.domain.user.model.dto;

import lombok.Data;

@Data
public class SingleResult<T> extends CommonResult {
    private T data;
}