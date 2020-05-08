package com.project.util;


import com.project.domain.user.service.UserAuthorityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil { // Jwt 토큰 생성 및 검증 모듈

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String REFRESH_TOKEN_REDIS_KEY = "refreshToken:";
    private static final String REFRESH_TOKEN_NAME = "X-REF-TOKEN";

    private final UserAuthorityService userAuthorityService;

    @Autowired
    public JwtTokenUtil(UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    @PostConstruct
    protected void init() { // was 기동 시 호출
        log.info("secretKey >>>>>: " + secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성(access token or refresh token)
    public String generateToken(Authentication authentication, String refreshTokenYn, long expireSecond) {
        String token = null;
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("authority", authentication.getAuthorities());
        Date nowDate = new Date();
        log.info("generateToken >>>>>>>>>>>>>>>>>>>>>>");

        if (refreshTokenYn.equals("Y")) {   // refresh token인 경우는 claims에 email을 설정하지 않음
            token = Jwts.builder()    // access 토큰 생성
                    .setIssuedAt(nowDate)
//                    .setExpiration(new Date(nowDate.getTime() + expireSecond * 1000))  // Expire date 셋팅
                    .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘, secret 값 셋팅
                    .compact();
        } else {
            token = Jwts.builder()    // access 토큰 생성
                    .setClaims(claims)  // 데이터(email + role)
                    .setIssuedAt(nowDate)
//                    .setExpiration(new Date(nowDate.getTime() + expireSecond * 1000))  // Expire date 셋팅
                    .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘, secret 값 셋팅
                    .compact();
        }

        if (refreshTokenYn.equals("Y")) {
            String key = REFRESH_TOKEN_REDIS_KEY + authentication.getName();
            RedisUtil.INSTANCE.set(key, token);    // refresh token 저장
        }
        return token;
    }

    // Jwt 토큰으로 인증정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userAuthorityService.loadUserByUsername(this.getUserPk(token));
        log.info("getAuthentication >>>>>>>>>>>>>>>>>>>>>>");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        log.info("getUserPk >>>>>>>>>>>>>>>>>>>>>>");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 쿠키에서 token 파싱 : "X-AUTH-TOKEN : Jwt 토큰"
    public String resolveToken(HttpServletRequest request, String jwtCookieName, String refreshTokenYn) {
        log.info("resolveToken >>>>>>>>>>>>>>>>>>>>>>");
        if (refreshTokenYn.equals("Y")) {
            return request.getHeader(REFRESH_TOKEN_NAME);
        } else {
            return CookieUtil.getValue(request, jwtCookieName);
        }

    }
    // Jwt access token의 유효성 + 만료일자 확인
    public boolean validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            long nowTime = new Date().getTime();
            long expireTime = claims.getBody().getIssuedAt().getTime() + 1000*60;
            if (expireTime > nowTime) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false;
        }
    }

    //
    public boolean validateRefreshToken(String refreshToken, String accessToken) {
        try {
            log.info("before claims :  " + accessToken);
            Jws<Claims> accessTokenClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            Jws<Claims> refreshTokenClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            log.info("accesstoken :  " + accessToken);
            log.info("claims :  " + accessTokenClaims);
            String redisKey = REFRESH_TOKEN_REDIS_KEY + accessTokenClaims.getBody().getSubject();  // refreshtoken: + email
            long nowTime = new Date().getTime();
            long expireTime = refreshTokenClaims.getBody().getIssuedAt().getTime() + 1000*60*60*24*14;
            if (refreshToken != null &&
                    refreshToken.equals(RedisUtil.INSTANCE.get(redisKey)) &&
                    expireTime>nowTime) {
                log.info("validateRefreshtoken true");
                return true;
            } else {
                log.info("refreshToken : " + refreshToken);
                log.info("refreshToken.equals(RedisUtil.INSTANCE.get(redisKey)) : " + refreshToken.equals(RedisUtil.INSTANCE.get(redisKey)));
                log.info("expireTime  : " + expireTime);
                log.info("nowTime  : " + nowTime);
                log.info("validateRefreshtoken else false");
                return false;
            }
        } catch(Exception e) {
            log.info("validateRefreshtoken exception false");
            return false;
        }
    }


/*    public boolean validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }*/
}
