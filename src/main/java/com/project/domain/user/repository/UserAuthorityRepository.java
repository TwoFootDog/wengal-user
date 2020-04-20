package com.project.domain.user.repository;

import com.project.domain.user.model.entity.UserAccount;
import com.project.domain.user.model.entity.UserAuthority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAuthorityRepository extends CrudRepository <UserAuthority, Long> {
    public List<UserAuthority> findAllByUserAccount(UserAccount userAccount);
}
