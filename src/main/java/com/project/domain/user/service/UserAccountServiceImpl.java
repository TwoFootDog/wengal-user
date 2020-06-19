package com.project.domain.user.service;

import com.project.domain.user.model.dto.*;
import com.project.domain.user.model.entity.UserAccount;
import com.project.domain.user.model.entity.UserAuthority;
import com.project.domain.user.repository.UserAccountRepository;
import com.project.domain.user.repository.UserAuthorityRepository;
import com.project.exception.ServiceException;
import com.project.util.CookieUtil;
import com.project.util.JwtTokenUtil;
import com.project.util.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = LogManager.getLogger(UserAccountService.class);

    @Value("${jwt.accessToken.expireSecond}")
    private long ACCESS_TOKEN_EXPIRE_SECOND ;    // access 토큰 유효시간(초)
    @Value(("${jwt.accessToken.name}"))
    private String ACCESS_TOKEN_NAME; // X-AUTH-TOKEN
    @Value("${cookie.expireSecond}")
    private int COOKIE_EXPIRE_SECOND;
    private static final String COOKIE_DOMAIN = "localhost";

    private final UserAccountRepository userAccountRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository,
                                  UserAuthorityRepository userAuthorityRepository,
                                  JwtTokenUtil jwtTokenUtil) {
        this.userAccountRepository = userAccountRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /* 로그인 함수 */
    public SingleResult<LoginResult> login(LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        String password = request.getPassword();

        logger.info("Login email : " + email + ", password : " + password);
        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findByEmail(email));
        userAccount.orElseThrow(()-> new ServiceException(false, -1, "계정정보가 존재하지 않습니다"));

        if (!BCrypt.checkpw(password, userAccount.get().getPassword())) {
            throw new ServiceException(false, -1, "비밀번호가 일치하지 않습니다");
        }
/*        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = null;
        logger.info("Login Token : " + token);
        try {
            authentication = authenticationManager.authenticate(token);  // userAuthorityService의 loadUserByUsername 호출
            logger.info("authentication : " + authentication);
        } catch (InternalAuthenticationServiceException e) {
            throw new ServiceException(false, -1, "계정정보가 존재하지 않습니다", e);
        } catch (BadCredentialsException e) {
            throw new ServiceException(false, -1, "비밀번호가 일치하지 않습니다", e);
        } catch (Exception e) {
            throw new ServiceException(false, -1, "시스템실 연락바람", e);
        }*/

        /* 인증받은 결과를 SecurityContextHolder에서 getContext()를 통해 context로 받아온 후,
        이 context(인증결과)를 set 해줌. 이로써 서버의 SecurityContext에는 인증값이 설정됨 */
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* Session의 속성값에 SecurityContext값을 넣어줌 */
        //httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        String accessToken;
        String refreshToken;
        try {
            /* JWT Access Token & refresh Token 생성 */
            accessToken = jwtTokenUtil.generateToken(userAccount.get().getUserId(), "A", ACCESS_TOKEN_EXPIRE_SECOND);
            // refreshToken = jwtTokenUtil.generateToken(email, "R", REFRESH_TOKEN_EXPIRE_DAY*24*60*60); 2020-06-20 refresh token은 추후 적용
            /* access token 용 쿠키 생성 */
            CookieUtil.create(response, ACCESS_TOKEN_NAME, accessToken, false, COOKIE_EXPIRE_SECOND, COOKIE_DOMAIN);

        } catch (Exception e) {
            throw new ServiceException(false, -1, "토큰 생성에 실패했습니다(redis 오류)");
        }
        return getSingleResult(
                new LoginResult(
                        userAccount.get().getUserId(),
                        userAccount.get().getEmail(),
                        userAccount.get().getNickname()));
        // return getSingleResult(new LoginResult(email, refreshToken));   2020-06-20 refresh token은 추후 적용
    }

    /* 로그아웃 함수 */
    public CommonResult logout(HttpServletRequest request, HttpServletResponse response) {
//        String accessToken = jwtTokenUtil.getToken(request, "A");
//        String userPk = jwtTokenUtil.getUserPk(accessToken);
        // long delCnt = jwtTokenUtil.deleteRefreshToken(userPk); 2020-06-20 refresh token은 추후 적용
        CookieUtil.clear(response, ACCESS_TOKEN_NAME); // access token 쿠키 삭제

        CommonResult commonResult = new CommonResult();
        setSuccessResult(commonResult);
        return commonResult;
    }


    /* 회원 등록 함수 */
    public SingleResult<SignUpResult> signUp(SignUpRequest request, HttpServletResponse response) throws Exception {
        SingleResult<SignUpResult> result;
        UserAccount userAccount = new UserAccount(
                request.getEmail(),
                BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()),  // 패스워드 암호화
                request.getNickname(),
                "signup",
                new Date(),
                "signup",
                new Date());
        try {
            UserAuthority userAuthority = userAuthorityRepository   // user 및 권한 등록
                    .save(new UserAuthority(userAccount, "USER", "signup", new Date(), "signup", new Date()));
            result = getSingleResult(
                    new SignUpResult(
                            userAuthority.getUserAccount().getUserId(),
                            userAuthority.getUserAccount().getEmail(),
                            userAuthority.getUserAccount().getNickname()));    // 단건 결과파일 생성

            String accessToken = jwtTokenUtil.generateToken(userAuthority.getUserAccount().getUserId(), "A", ACCESS_TOKEN_EXPIRE_SECOND);
            CookieUtil.create(response, ACCESS_TOKEN_NAME, accessToken, false, COOKIE_EXPIRE_SECOND, COOKIE_DOMAIN);    // access token 생성 후 쿠키 저장

        } catch (Exception e) {
            logger.error("signUp >> userAuthorityRepository.save() Fail");
            throw new ServiceException(false, -1, "회원가입에 실패했습니다.", e);
        }
        logger.info("signUp Success. userId : " + userAccount.getUserId() + ", email : " + userAccount.getEmail() + ", nickName : " + userAccount.getNickname());
        return result;
    }

    /* 회원 탈회 함수 */
    @Transactional
    public SingleResult<DeleteAccountResult> deleteAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String accessToken = jwtTokenUtil.getToken(request, "A");
        String userPk = jwtTokenUtil.getUserPk(accessToken);
        CookieUtil.clear(response, ACCESS_TOKEN_NAME); // access token 쿠키 삭제

        SingleResult<DeleteAccountResult> result;
        try {
            int cnt = userAuthorityRepository.deleteByUserAccount(userAccountRepository.findByUserId(userPk));
//            int cnt = userAccountRepository.deleteByUserId(userPk);
            result = getSingleResult(new DeleteAccountResult(userPk, cnt));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteAccount >> userRepository.deleteById Fail. userId : " + userPk);
            throw new ServiceException(false, -1, "탈회에 실패했습니다.", e);
        }
        logger.error("deleteAccount Success. userId : " + userPk);
        return result;
    }

    /* 2020-06-20 refresh token 로직은 추후 적용 */
    /* 회원 refresh token 체크 함수
    refresh token 이 유효하면 access token 재발급
    refresh token 이 유효하지 않으면 에러 */
    public CommonResult refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessToken = jwtTokenUtil.getToken(request, "A");
        String refreshToken = jwtTokenUtil.getToken(request, "R");

        boolean isRefreshTokenValid = jwtTokenUtil.validateRefreshToken(refreshToken, accessToken);
        if(isRefreshTokenValid) {   // refresh token 이 유입되었고 유효한 경우
            String email = jwtTokenUtil.getUserPk(accessToken);
            /* JWT Access Token 생성 */
            String accessToken1 = jwtTokenUtil.generateToken(email, "N", ACCESS_TOKEN_EXPIRE_SECOND);

            /* access token 용 쿠키 생성 */
            CookieUtil.create(response, ACCESS_TOKEN_NAME, accessToken1, false, COOKIE_EXPIRE_SECOND, COOKIE_DOMAIN);
        } else {
            throw new ServiceException(false, -1, "토큰 발급에 실패했습니다");
        }
        CommonResult commonResult = new CommonResult();
        setSuccessResult(commonResult);
        return commonResult;
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
