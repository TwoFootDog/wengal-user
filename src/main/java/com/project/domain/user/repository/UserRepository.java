package com.project.domain.user.repository;

import com.project.domain.user.model.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
