package com.solver.solver_be.global.security.jwt;

import com.solver.solver_be.global.type.ErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.resolveToken(request, "Access");
        String refreshToken = jwtUtil.resolveToken(request, "Refresh");

        // 1. AccessToken Not Found
        if (accessToken == null) {
            request.setAttribute("exception", ErrorType.TOKEN_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        }

        // 2. AccessToken Not Valid
        if (!jwtUtil.validateToken(accessToken)) {
            // AccessToken Expire And AccessToken Re-issuance
            if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
                String userEmail = jwtUtil.getUserId(refreshToken);
                String newAccessToken = jwtUtil.createToken(userEmail, "Access");
                response.addHeader(JwtUtil.ACCESS_TOKEN, newAccessToken);
                this.setAuthentication(userEmail);
            } else {
                // RefreshToken Not Valid
                request.setAttribute("exception", ErrorType.NOT_VALID_REFRESH_TOKEN);
                filterChain.doFilter(request, response);
                return;
            }
            request.setAttribute("exception", ErrorType.NOT_VALID_TOKEN);
            filterChain.doFilter(request, response);
            return;
        }

        Claims info = jwtUtil.getUserInfoFromToken(accessToken);
        try {
            setAuthentication(info.getSubject());
        } catch (UsernameNotFoundException e) {
            request.setAttribute("exception", ErrorType.USER_NOT_FOUND);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String userEmail) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(userEmail);// 인증 객체 생성
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}