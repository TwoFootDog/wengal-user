package com.project.domain.user.repository;

import com.project.domain.user.model.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    public UserAccount findByEmail(String email);
}
