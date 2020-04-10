package com.project.domain.user.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> {
    private T data;
}
