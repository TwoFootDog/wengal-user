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
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil { // Jwt 토큰 생성 및 검증 모듈

    @Value("${jwt.secret}")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 60;    // 토큰 유효시간 : 1시간
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

    // Jwt 토큰 생성
    public String generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("authority", authentication.getAuthorities());
        Date nowDate = new Date();
        log.info("generateToken >>>>>>>>>>>>>>>>>>>>>>");
        return Jwts.builder()
                .setClaims(claims)  // 데이터
                .setIssuedAt(nowDate)
                .setExpiration(new Date(nowDate.getTime() + tokenValidMilisecond))  // Expire date 셋팅
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘, secret 값 셋팅
                .compact();
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

    // Request Header에서 token 파싱 : "X-AUTH-TOKEN : Jwt 토큰"
    public String resolveToken(HttpServletRequest request, String jwtCookieName) {
        log.info("resolveToken >>>>>>>>>>>>>>>>>>>>>>");
        return CookieUtil.getValue(request, jwtCookieName);
//        return req.getHeader("X-AUTH-TOKEN");
    }
    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }
}
