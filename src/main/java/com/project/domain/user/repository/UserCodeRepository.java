package com.project.domain.user.repository;

import com.project.domain.user.model.entity.UserCode;
import org.springframework.data.repository.CrudRepository;

public interface UserCodeRepository extends CrudRepository <UserCode, Long> {
    public UserCode findByCodeValue(String codeValue);
}
