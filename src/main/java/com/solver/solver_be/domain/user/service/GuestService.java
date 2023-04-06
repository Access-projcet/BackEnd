package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.user.dto.*;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.entity.UserRoleEnum;
import com.solver.solver_be.domain.user.repository.AdminRepository;
import com.solver.solver_be.domain.user.repository.GuestRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import com.solver.solver_be.global.security.jwt.JwtUtil;
import com.solver.solver_be.global.security.refreshtoken.RefreshToken;
import com.solver.solver_be.global.security.refreshtoken.RefreshTokenRepository;
import com.solver.solver_be.global.security.refreshtoken.TokenDto;
import com.solver.solver_be.global.util.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final JwtUtil jwtUtil;
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
            throw new UserException(ResponseCode.USER_ID_EXIST);
        }

        // UserRole Check
        UserRoleEnum role = UserRoleEnum.GUEST;

        // Save Entity
        guestRepository.save(Guest.of(signupRequestDto, password, role));

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.SIGN_UP_SUCCESS));
    }

    // 2. Guest Login
    @Transactional
    public ResponseEntity<GlobalResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        // User Existed Check
        if (guestRepository.findByUserId(loginRequestDto.getUserId()).isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // Password Decode Check
        Guest guest = guestRepository.findByUserId(loginRequestDto.getUserId()).get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), guest.getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
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

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.LOG_IN_SUCCESS, LoginResponseDto.of(guest)));
    }

    // 3. Guest change password
    @Transactional
    public ResponseEntity<GlobalResponseDto> changePassword(PasswordChangeRequestDto passwordChangeRequestDto, Guest guest) {

        // User Existed Check
        Optional<Guest> guestFound = guestRepository.findByUserId(guest.getUserId());
        if (guestFound.isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // Current Password Check
        if (!passwordEncoder.matches(passwordChangeRequestDto.getPassword(), guestFound.get().getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
        }

        // New Password Set
        String newPasswordEncoded = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());

        // Password Update
        guest.setPassword(newPasswordEncoded);
        guestRepository.save(guest);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
    }

    // 4. Found Guest userId
    @Transactional
    public ResponseEntity<GlobalResponseDto> findGuestSearchId(UserSearchRequestDto userSearchRequestDto) throws MessagingException {

        // Find your ID by name and phone number
        Guest guest = guestRepository.findGuestByNameAndPhoneNum(userSearchRequestDto.getName(), userSearchRequestDto.getPhoneNum());
        if (guest == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }

        // Send ID After Email Authentication
        if (emailService.verifyEmailCode(userSearchRequestDto.getEmail(), userSearchRequestDto.getCode())) {
            emailService.sendUserSearchEmail(userSearchRequestDto.getEmail(), guest.getUserId());
        }else {
            throw new UserException(ResponseCode.AUTH_FAILED);
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.FIND_USER_ID));
    }

    // 5. Reset Guest Password
    @Transactional
    public ResponseEntity<GlobalResponseDto> resetGuestPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {

        Guest guest = guestRepository.findGuestByNameAndPhoneNumAndUserId(passwordResetRequestDto.getName(), passwordResetRequestDto.getPhoneNum(), passwordResetRequestDto.getUserId());

        if (guest == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }

        // Send a new password after email authentication
        if (emailService.verifyEmailCode(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getCode())) {

            // Provisional Password Issue
            String newPassword = generateRandomPassword();

            // Replace existing password
            guest.setPassword(passwordEncoder.encode(newPassword));
            guestRepository.save(guest);

            emailService.sendPasswordResetEmail(passwordResetRequestDto.getEmail(), newPassword);
        }else {
            throw new UserException(ResponseCode.AUTH_FAILED);
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
    }

    // Method : Generating a temporary password
    private String generateRandomPassword() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
