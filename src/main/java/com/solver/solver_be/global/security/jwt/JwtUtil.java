package com.solver.solver_be.global.security.jwt;

import com.solver.solver_be.global.security.jwt.dto.TokenDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsServiceImpl;
import com.solver.solver_be.global.util.redis.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisUtil redisUtil;
    private static final long ACCESS_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Authorization";
    public static final String REFRESH_TOKEN = "RefreshToken";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Get Header From Token
    public String resolveToken(HttpServletRequest request, String type) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.debug(headerName + ": " + headerValue);
        }

        String bearerToken = type.equals("Access") ? request.getHeader(ACCESS_TOKEN) : request.getHeader(REFRESH_TOKEN);
        if (bearerToken == null) {
            return null;
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Create All Token
    public TokenDto createAllToken(String userEmail) {
        return new TokenDto(createToken(userEmail, "Access"), createToken(userEmail, "Refresh"));
    }

    // Create Token (Access / Refresh)
    public String createToken(String userEmail, String type) {

        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userEmail)
                        .setExpiration(new Date(date.getTime() + time))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // Token Validate
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // RefreshToken Validate
    public Boolean validateRefreshToken(String refreshToken){

        if (!validateToken(refreshToken)){
            return false;
        }

        String findUserId = getUserId(refreshToken);
        String redisData = redisUtil.getData(findUserId);

        return redisData != null && redisData.equals("Bearer " + refreshToken);
    }

    // Get Token From UserData
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Get Token From UserId
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Create Authentication
    public Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // Add Header To AccessToken, RefreshToken
    public void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}
