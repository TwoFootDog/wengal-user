package com.project.domain.user.service;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.model.entity.UserAccount;
import com.project.domain.user.model.entity.UserAuthority;
import com.project.domain.user.repository.UserAccountRepository;
import com.project.domain.user.repository.UserAuthorityRepository;
import com.project.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = LogManager.getLogger(UserAccountService.class);
    private final UserAccountRepository userAccountRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository,
                                  UserAuthorityRepository userAuthorityRepository,
                                  AuthenticationManager authenticationManager) {
        this.userAccountRepository = userAccountRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.authenticationManager = authenticationManager;
    }

    /* 로그인 함수 */
    public SingleResult<LoginResult> login(LoginRequest request, HttpSession httpSession) {

        String email = request.getEmail();
        String password = request.getPassword();
        logger.info("Login email : " + email + ", password : " + password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = null;
        logger.info("Login Token : " + token);
        try {
            authentication = authenticationManager.authenticate(token);  // userAuthService의 loadUserByUsername 호출
            logger.info("authentication : " + authentication);
        } catch (InternalAuthenticationServiceException e) {
            throw new ServiceException(false, -1, "계정정보가 존재하지 않습니다", e);
        } catch (BadCredentialsException e) {
            throw new ServiceException(false, -1, "비밀번호가 일치하지 않습니다", e);
        } catch (Exception e) {
            throw new ServiceException(false, -1, "시스템실 연락바람", e);
        }

        /* 인증받은 결과를 SecurityContextHolder에서 getContext()를 통해 context로 받아온 후,
        이 context(인증결과)를 set 해줌. 이로써 서버의 SecurityContext에는 인증값이 설정됨 */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* Session의 속성값에 SecurityContext값을 넣어줌 */
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return getSingleResult(new LoginResult(authentication.getName(), token.getName()));
    }


    /* 회원 등록 함수 */
    public SingleResult<SignUpResult> signUp(SignUpRequest request) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    // 패스워드 인코더
        SingleResult<SignUpResult> result;
        UserAccount userAccount = new UserAccount(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),  // 패스워드 암호화
                request.getNickname(),
                "signup",
                new Date(),
                "signup",
                new Date());
        try {
            UserAuthority userAuthority = userAuthorityRepository   // user 및 권한 등록
                    .save(new UserAuthority(userAccount, "USER", "signup", new Date(), "signup", new Date()));
            result = getSingleResult(new SignUpResult(userAuthority.getUserAccount().getEmail(), userAuthority.getUserAccount().getNickname()));    // 단건 결과파일 생성
        } catch (Exception e) {
            logger.error("signUp >> userAuthorityRepository.save() Fail");
            throw new ServiceException(false, -1, "회원가입에 실패했습니다.", e);
        }
        logger.info("signUp Success. userId : " + userAccount.getId() + ", email : " + userAccount.getEmail() + ", nickName : " + userAccount.getNickname());
        return result;
    }

    /* 회원 탈회 함수 */
    public SingleResult<DeleteAccountResult> deleteAccount(Long userId) throws Exception {
        SingleResult<DeleteAccountResult> result;
        try {
            userAccountRepository.deleteById(userId);
            result = getSingleResult(new DeleteAccountResult(userId));
        } catch (Exception e) {
            logger.error("deleteAccount >> userRepository.deleteById Fail. userId : " + userId);
            throw new ServiceException(false, -1, "탈회에 실패했습니다.", e);
        }
        logger.error("deleteAccount Success. userId : " + userId);
        return result;
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
