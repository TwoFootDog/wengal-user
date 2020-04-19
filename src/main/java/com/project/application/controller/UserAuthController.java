package com.project.application.controller;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.service.UserAuthorityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserAuthController {

    private static final Logger logger = LogManager.getLogger(UserAuthController.class);
    private final UserAuthorityService userAuthorityService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public UserAuthController(UserAuthorityService userAuthorityService, AuthenticationManager authenticationManager) {
        this.userAuthorityService = userAuthorityService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public SingleResult<LoginResult> login(@RequestBody LoginRequest request, HttpSession httpSession) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = request.getEmail();
        String password = request.getPassword();
        logger.info("Login email : " + email + ", password : " + password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        logger.info("Login Token : " + token);
        Authentication authentication = authenticationManager.authenticate(token);  // userAuthService의 loadUserByUsername 호출
        logger.info("authentication : " + authentication);

        /* 인증받은 결과를 SecurityContextHolder에서 getContext()를 통해 context로 받아온 후,
        이 context(인증결과)를 set 해줌. 이로써 서버의 SecurityContext에는 인증값이 설정됨 */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* Session의 속성값에 SecurityContext값을 넣어줌 */
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return new SingleResult<LoginResult>();
    }

    /* 단건 결과파일 전송 */
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<T>();
        result.setData(data);
        setSuccessResult(result);   // 공통영역 셋팅(success)
        return result;
    }

    /* 성공 시 메시지 및 코드 셋팅하는 함수 */
    private void setSuccessResult(CommonResult result) {
        result.setCode(0);
        result.setMsg("SUCCESS");
        result.setSuccess(true);
    }
}
