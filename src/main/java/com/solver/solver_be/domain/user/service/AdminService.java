package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.company.repository.CompanyRepository;
import com.solver.solver_be.domain.user.dto.AdminSignupRequestDto;
import com.solver.solver_be.domain.user.dto.LoginRequestDto;
import com.solver.solver_be.domain.user.dto.LoginResponseDto;
import com.solver.solver_be.domain.user.dto.PasswordChangeRequestDto;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.UserRoleEnum;
import com.solver.solver_be.domain.user.repository.AdminRepository;
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
public class AdminService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 1. Admin SignUp
    @Transactional
    public ResponseEntity<GlobalResponseDto> signupBusiness(AdminSignupRequestDto signupRequestDto) {

        // 1) Password Encode
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 2) Duplicated User Check
        Optional<Admin> found = adminRepository.findByUserId(signupRequestDto.getUserId());
        if (found.isPresent()) {
            throw new UserException(ResponseCode.USER_ID_EXIST);
        }

        Optional<Company> foundCompany = companyRepository.findByCompanyTokenAndCompanyName(signupRequestDto.getCompanyToken(), signupRequestDto.getCompanyName());

        // 3) CompanyToken Check
        if (!foundCompany.isPresent()) {
            throw new UserException(ResponseCode.INVALID_COMPANY_TOKEN);
        }
        Company company = companyRepository.findByCompanyToken(signupRequestDto.getCompanyToken());
        UserRoleEnum role = UserRoleEnum.ADMIN;

        // 4) Save Admin Entity
        adminRepository.save(Admin.of(signupRequestDto, password, role, company));

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.SIGN_UP_SUCCESS));
    }

    // 2. Admin Login
    @Transactional
    public ResponseEntity<GlobalResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        // 1) User Existed Check
        if (adminRepository.findByUserId(loginRequestDto.getUserId()).isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // 2) Password Decode Check
        Admin admin = adminRepository.findByUserId(loginRequestDto.getUserId()).get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), admin.getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
        }

        // 3) Granting AccessToken
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getUserId());

        // 4) Granting RefreshToken
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

        // 1) User Existed Check
        Optional<Admin> adminFound = adminRepository.findByUserId(admin.getUserId());
        if (adminFound.isEmpty()) {
            throw new UserException(ResponseCode.USER_ACCOUNT_NOT_EXIST);
        }

        // 2) Current Password Check
        if (!passwordEncoder.matches(passwordChangeRequestDto.getPassword(), adminFound.get().getPassword())) {
            throw new UserException(ResponseCode.PASSWORD_MISMATCH);
        }

        // 3) New Password Set
        String newPasswordEncoded = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());

        // 4) Password Update
        admin.setPassword(newPasswordEncoded);
        adminRepository.save(admin);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
    }
}
