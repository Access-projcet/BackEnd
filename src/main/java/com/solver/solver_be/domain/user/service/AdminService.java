package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.company.repository.CompanyRepository;
import com.solver.solver_be.domain.user.dto.*;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.entity.QGuest;
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
public class AdminService {

    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final GuestRepository guestRepository;
    private final CompanyRepository companyRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 1. Admin SignUp
    @Transactional
    public ResponseEntity<GlobalResponseDto> signupBusiness(AdminSignupRequestDto signupRequestDto) {

        // Password Encode
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // Duplicated User Check
        Optional<Admin> foundAdmin = adminRepository.findByUserId(signupRequestDto.getUserId());
        Optional<Guest> foundGuest = guestRepository.findByUserId(signupRequestDto.getUserId());
        if (foundAdmin.isPresent() || foundGuest.isPresent()) {
            throw new UserException(ResponseCode.USER_ID_EXIST);
        }

        // Get Company By CompanyToken And CompanyName
        Optional<Company> company = companyRepository.findByCompanyTokenAndCompanyName(signupRequestDto.getCompanyToken(), signupRequestDto.getCompanyName());
        if (company.isEmpty()) {
            throw new UserException(ResponseCode.INVALID_COMPANY_TOKEN);
        }

        // Give Admin UserRole
        UserRoleEnum role = UserRoleEnum.ADMIN;

        // AdminRepo Save
        adminRepository.save(Admin.of(signupRequestDto, password, role, company.get()));

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.SIGN_UP_SUCCESS));
    }

    // 2. Admin Login
    @Transactional
    public ResponseEntity<GlobalResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        // Admin Existed Check
        if (adminRepository.findByUserId(loginRequestDto.getUserId()).isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // Password Decode Check
        Admin admin = adminRepository.findByUserId(loginRequestDto.getUserId()).get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), admin.getPassword())) {
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

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.LOG_IN_SUCCESS, LoginResponseDto.of(admin)));
    }

    // 3. Admin change password
    @Transactional
    public ResponseEntity<GlobalResponseDto> changePassword(PasswordChangeRequestDto passwordChangeRequestDto, Admin admin) {

        // Admin Existed Check
        Optional<Admin> adminFound = adminRepository.findByUserId(admin.getUserId());
        if (adminFound.isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // Current Password Check
        if (!passwordEncoder.matches(passwordChangeRequestDto.getPassword(), adminFound.get().getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
        }

        // New Password Set
        String newPasswordEncoded = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());

        // Password Update
        admin.setPassword(newPasswordEncoded);
        adminRepository.save(admin);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
    }

    // 4. Found Admin userId
    @Transactional
    public ResponseEntity<GlobalResponseDto> findAdminSearchId(UserSearchRequestDto userSearchRequestDto) throws MessagingException {

        // Find your ID by name and phone number
        Admin admin = adminRepository.findAdminByNameAndPhoneNum(userSearchRequestDto.getName(), userSearchRequestDto.getPhoneNum());
        if (admin == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }

        // Send ID After Email Authentication
        if (emailService.verifyEmailCode(userSearchRequestDto.getEmail(), userSearchRequestDto.getCode())) {
            emailService.sendUserSearchEmail(userSearchRequestDto.getEmail(), admin.getUserId());
        }else {
            throw new UserException(ResponseCode.AUTH_FAILED);
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.FIND_USER_ID));
    }

    // 5. Reset Admin Password
    @Transactional
    public ResponseEntity<GlobalResponseDto> resetAdminPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {

        Admin admin = adminRepository.findAdminByNameAndPhoneNumAndUserId(passwordResetRequestDto.getName(), passwordResetRequestDto.getPhoneNum(), passwordResetRequestDto.getUserId());

        if (admin == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }

        // Send a new password after email authentication
        if (emailService.verifyEmailCode(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getCode())) {

            // Provisional Password Issue
            String newPassword = generateRandomPassword();

            // Replace existing password
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);

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
