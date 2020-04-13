package com.project.domain.user.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ListResult<T> extends CommonResult {
    private int gridCnt;    // list 건수
    private List<T> list;
}

