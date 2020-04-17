package com.project.domain.user.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SingleResult2 extends CommonResult {
    Map<String, Object> data;
}
