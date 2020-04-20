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
        logger.info("loadUserByUsername call. username : " + username);
        UserAccount userAccount = userAccountRepository.findByEmail(username);
        logger.info("loadUserByUsername call. userAccount : " + userAccount);
        User user = null;
        try {
            user = new User(userAccount.getEmail(), userAccount.getPassword(), makeGrantedAuthority(userAuthorityRepository.findAllByUserAccount(userAccount)));
        } catch (Exception e ) {
            e.printStackTrace();
        }

        logger.info("loadUserByUsername call. user : " + user);
        return user;
    }


    @Override
    public List<GrantedAuthority> makeGrantedAuthority(List<UserAuthority> authorities) {
        logger.info("makeGrantedAuthority>>>>>");
        List<GrantedAuthority> list = new ArrayList<>();
        authorities.forEach(authority->list.add(new SimpleGrantedAuthority(ROLE_PREFIX + authority.getUserCode().getCodeValue())));
        authorities.forEach(authority->logger.info("authority.getUserCode().getCodeValue() : " + authority.getUserCode().getCodeValue()));
       logger.info("authority.getUserCode().getCodeValue() : " + authorities.get(0).getUserCode().getCodeValue());
        logger.info("authority.getUserCode().getCodeValue() : " + authorities.get(1).getUserCode().getCodeValue());
        return list;
    }
}
