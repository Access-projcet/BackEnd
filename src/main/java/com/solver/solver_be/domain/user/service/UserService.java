package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.user.dto.LoginRequestDto;
import com.solver.solver_be.domain.user.dto.SignupRequestDto;
import com.solver.solver_be.domain.user.entity.User;
import com.solver.solver_be.domain.user.dto.LoginResponseDto;
import com.solver.solver_be.domain.user.entity.UserRoleEnum;
import com.solver.solver_be.domain.user.repository.UserRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import com.solver.solver_be.global.security.jwt.JwtUtil;
import com.solver.solver_be.global.security.refreshtoken.RefreshToken;
import com.solver.solver_be.global.security.refreshtoken.RefreshTokenRepository;
import com.solver.solver_be.global.security.refreshtoken.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //회원 가입
    @Transactional
    public ResponseEntity<GlobalResponseDto> signup(SignupRequestDto signupRequestDto) {
        String userId = signupRequestDto.getUserId();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //회원 중복 확인
        Optional<User> found = userRepository.findByUserEmail(userId);
        if (found.isPresent()) {
            throw new UserException(ResponseCode.USER_EMAIL_EXIST);
        }

        // 사용자 권한
        UserRoleEnum role = UserRoleEnum.USER;

        User user = User.of(userId, password, role);
        userRepository.save(user);
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.SIGN_UP_SUCCESS));
    }

    // 로그인
    @Transactional
    public ResponseEntity<GlobalResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String userEmail = loginRequestDto.getUserEmail();
        String password = loginRequestDto.getPassword();

        if (userRepository.findByUserEmail(userEmail).isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        User user = userRepository.findByUserEmail(userEmail).get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(userEmail);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findAllByUserEmail(userEmail);

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), userEmail);
            refreshTokenRepository.save(newToken);
        }

        jwtUtil.setHeader(response, tokenDto);
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.LOG_IN_SUCCESS, LoginResponseDto.of(user)));
    }

}
