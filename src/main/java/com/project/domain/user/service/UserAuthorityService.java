package com.project.domain.user.service;

import com.project.domain.user.model.dto.LoginRequest;
import com.project.domain.user.model.dto.LoginResult;
import com.project.domain.user.model.dto.SingleResult;
import com.project.domain.user.model.entity.UserAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserAuthorityService extends UserDetailsService {
    public List<GrantedAuthority> makeGrantedAuthority(List<UserAuthority> authoritys);
}
