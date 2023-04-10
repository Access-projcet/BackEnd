package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.user.dto.requestDto.*;
import com.solver.solver_be.domain.user.dto.responseDto.LoginResponseDto;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.repository.AdminRepository;
import com.solver.solver_be.domain.user.repository.GuestRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.jwt.JwtUtil;
import com.solver.solver_be.global.security.refreshtoken.RefreshToken;
import com.solver.solver_be.global.security.refreshtoken.RefreshTokenRepository;
import com.solver.solver_be.global.security.refreshtoken.TokenDto;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import com.solver.solver_be.global.type.UserRoleEnum;
import com.solver.solver_be.global.util.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final JwtUtil jwtUtil;
    private final InfoProvider infoProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final GuestRepository guestRepository;
    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 1. Guest SignUp
    @Transactional
    public ResponseEntity<GlobalResponseDto> signupGuest(GuestSignupRequestDto signupRequestDto) {

        // Password Encode
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // Duplicated User Check
        Optional<Admin> foundAdmin = adminRepository.findByUserId(signupRequestDto.getUserId());
        Optional<Guest> foundGuest = guestRepository.findByUserId(signupRequestDto.getUserId());
        if (foundAdmin.isPresent() || foundGuest.isPresent()) {
            throw new UserException(ErrorType.USER_ID_EXIST);
        }

        // Password Equals Check
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getCheckPassword())){
            throw new UserException(ErrorType.PASSWORD_MISMATCH);
        }

        // UserRole Check
        UserRoleEnum role = UserRoleEnum.GUEST;

        // Save Entity
        guestRepository.save(Guest.of(signupRequestDto, password, role));

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.SIGN_UP_SUCCESS));
    }

    // 2. Guest Login
    @Transactional
    public ResponseEntity<GlobalResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        // User Existed Check
        if (guestRepository.findByUserId(loginRequestDto.getUserId()).isEmpty()) {
            throw new UserException(ErrorType.USER_ACCOUNT_NOT_EXIST);
        }

        // Password Decode Check
        Guest guest = guestRepository.findByUserId(loginRequestDto.getUserId()).get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), guest.getPassword())) {
            throw new UserException(ErrorType.PASSWORD_MISMATCH);
        }

        // Granting AccessToken
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getUserId());

        // Granting RefreshToken
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findAllByUserEmail(loginRequestDto.getUserId());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getUserId());
            refreshTokenRepository.save(newToken);
        }
        jwtUtil.setHeader(response, tokenDto);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.LOG_IN_SUCCESS, LoginResponseDto.of(guest)));
    }

    // 3. Guest change password
    @Transactional
    public ResponseEntity<GlobalResponseDto> changePassword(PasswordChangeRequestDto passwordChangeRequestDto, Guest guest) {

        // User Existed Check
        Optional<Guest> guestFound = guestRepository.findByUserId(guest.getUserId());
        if (guestFound.isEmpty()) {
            throw new UserException(ErrorType.USER_ACCOUNT_NOT_EXIST);
        }

        // Current Password Check
        if (!passwordEncoder.matches(passwordChangeRequestDto.getPassword(), guestFound.get().getPassword())) {
            throw new UserException(ErrorType.PASSWORD_MISMATCH);
        }

        // NewPassword Equals Check
        if (!passwordChangeRequestDto.getNewPassword().equals(passwordChangeRequestDto.getCheckPassword())){
            throw new UserException(ErrorType.PASSWORD_MISMATCH);
        }

        // New Password Set
        String newPasswordEncoded = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());

        // Password Update
        guest.setPassword(newPasswordEncoded);
        guestRepository.save(guest);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.PASSWORD_RESET_SUCCESS));
    }

    @Transactional
    public ResponseEntity<GlobalResponseDto> findGuestUserData(UserSearchRequestDto userSearchRequestDto) throws MessagingException {

        // Find guest data by name and phone number
        Guest guest = guestRepository.findGuestByNameAndPhoneNum(userSearchRequestDto.getName(), userSearchRequestDto.getPhoneNum());
        if (guest == null) {
            throw new UserException(ErrorType.USER_NOT_FOUND);
        }

        // Send Guest UserId
        emailService.sendUserSearchEmail(userSearchRequestDto.getEmail(), guest.getUserId());

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.FIND_USER_ID));
    }

    // 5. Reset Guest Password
    @Transactional
    public ResponseEntity<GlobalResponseDto> resetGuestPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {

        Guest guest = guestRepository.findGuestByNameAndPhoneNumAndUserId(passwordResetRequestDto.getName(), passwordResetRequestDto.getPhoneNum(), passwordResetRequestDto.getUserId());

        if (guest == null) {
            throw new UserException(ErrorType.USER_NOT_FOUND);
        }

        // Provisional Password Issue
        String newPassword = infoProvider.generateRandomPassword();

        // Replace existing password
        guest.setPassword(passwordEncoder.encode(newPassword));
        guestRepository.save(guest);

        emailService.sendPasswordResetEmail(passwordResetRequestDto.getEmail(), newPassword);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.PASSWORD_RESET_SUCCESS));
    }
}
