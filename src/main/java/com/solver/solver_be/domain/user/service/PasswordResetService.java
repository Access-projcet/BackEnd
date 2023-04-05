package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.user.dto.PasswordResetRequestDto;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.repository.AdminRepository;
import com.solver.solver_be.domain.user.repository.GuestRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import com.solver.solver_be.global.util.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final GuestRepository guestRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // 1. Reset Guest Password
    public ResponseEntity<GlobalResponseDto> resetGuestPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {

        Guest guest = guestRepository.findGuestByNameAndPhoneNumAndUserId(passwordResetRequestDto.getName(), passwordResetRequestDto.getPhoneNum(), passwordResetRequestDto.getUserId());

        if (guest == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        } else {

            // Provisional Password Issue
            String newPassword = generateRandomPassword();

            // Replace existing password
            guest.setPassword(passwordEncoder.encode(newPassword));
            guestRepository.save(guest);

            // Send a new password after email authentication
            if (emailService.verifyEmailCode(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getCode())) {
                emailService.sendPasswordResetEmail(passwordResetRequestDto.getEmail(), newPassword);
            }else {
                throw new UserException(ResponseCode.AUTH_FAILED);
            }

            return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
        }
    }

    // 2. Reset Admin Password
    public ResponseEntity<GlobalResponseDto> resetAdminPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {

        Admin admin = adminRepository.findAdminByNameAndPhoneNumAndUserId(passwordResetRequestDto.getName(), passwordResetRequestDto.getPhoneNum(), passwordResetRequestDto.getUserId());

        if (admin == null) {
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        } else {

            // Provisional Password Issue
            String newPassword = generateRandomPassword();

            // Replace existing password
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);

            // Send a new password after email authentication
            if (emailService.verifyEmailCode(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getCode())) {
            emailService.sendPasswordResetEmail(passwordResetRequestDto.getEmail(), newPassword);
            }else {
            throw new UserException(ResponseCode.AUTH_FAILED);
            }

            return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.PASSWORD_RESET_SUCCESS));
        }
    }

    // Generating a temporary password
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
