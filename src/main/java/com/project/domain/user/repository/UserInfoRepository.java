package com.project.domain.user.repository;

import com.project.domain.user.model.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
}
