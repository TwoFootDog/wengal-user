package com.project.domain.user.service;

import com.project.domain.user.model.entity.UserAccount;
import com.project.domain.user.model.entity.UserAuthority;
import com.project.domain.user.repository.UserAccountRepository;
import com.project.domain.user.repository.UserAuthorityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    private static final Logger logger = LogManager.getLogger(UserAuthorityService.class);
    private static final String ROLE_PREFIX = "ROLE_";

    private final UserAccountRepository userAccountRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    @Autowired
    public UserAuthorityServiceImpl(
            UserAccountRepository userAccountRepository,
            UserAuthorityRepository userAuthorityRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUserName>>>");
        UserAccount userAccount = userAccountRepository.findByEmail(username);  // email로 계정정보 검색
        User user = new User(userAccount.getEmail(), userAccount.getPassword(), makeGrantedAuthority(userAuthorityRepository.findAllByUserAccount(userAccount)));
        return user;
    }

    @Override
    public List<GrantedAuthority> makeGrantedAuthority(List<UserAuthority> authorities) {
        List<GrantedAuthority> list = new ArrayList<>();
        authorities.forEach(authority->list.add(new SimpleGrantedAuthority(ROLE_PREFIX + authority.getAuthName())));
        return list;
    }
}
