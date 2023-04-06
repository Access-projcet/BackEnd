package com.solver.solver_be.domain.user.service;

import com.solver.solver_be.domain.user.dto.UserSearchRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final GuestRepository guestRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    // 1. Found Guest userId
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

    // 2. Found Admin userId
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
}