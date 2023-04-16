package com.solver.solver_be.domain.company.service;

import com.solver.solver_be.domain.company.dto.CompanyRequestDto;
import com.solver.solver_be.domain.company.dto.CompanyResponseDto;
import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.company.repository.CompanyRepository;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.global.exception.exceptionType.CompanyException;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private static final int ID_LENGTH = 16;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 1. Create Company
    @Transactional
    public ResponseEntity<GlobalResponseDto> registerCompany(CompanyRequestDto companyRequestDto) {

        // Company Duplicate Validation
        if (companyRepository.findByCompanyName(companyRequestDto.getCompanyName()).isPresent()) {
            throw new UserException(ErrorType.COMPANY_ALREADY_EXIST);
        }

        // Create Company Token
        String companyToken = createCompanyToken();

        // CompanyRepo Save
        Company company = companyRepository.saveAndFlush(Company.of(companyRequestDto, companyToken));

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.COMPANY_REGISTER_SUCCESS, CompanyResponseDto.of(company)));
    }

    // 2. Get Company List
    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getCompanies(Guest guest) {

        // Get CompanyList
        List<Company> companyList = companyRepository.findAllByOrderByCreatedAtDesc();

        // CompanyList Validation
        if (companyList.isEmpty()) {
            throw new CompanyException(ErrorType.COMPANY_NOT_FOUND);
        }

        // Create CompanyResponseDtoList
        List<CompanyResponseDto> companyResponseDtoList = new ArrayList<>();
        for (Company company : companyList) {
            companyResponseDtoList.add(CompanyResponseDto.of(company));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.COMPANY_GET_SUCCESS, companyResponseDtoList));
    }

    // 3. Update Company Info
    @Transactional
    public ResponseEntity<GlobalResponseDto> updateCompany(Long id, CompanyRequestDto companyRequestDto, Admin admin) {

        // Get Company By id
        Optional<Company> company = companyRepository.findById(id);

        // Company Validation
        if (company.isEmpty()) {
            throw new CompanyException(ErrorType.COMPANY_NOT_FOUND);
        }

        // CompanyRepo Update
        company.get().update(companyRequestDto);

        // Create Company ResponseDto
        CompanyResponseDto companyResponseDto = CompanyResponseDto.of(company.get());

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.COMPANY_UPDATE_SUCCESS, companyResponseDto));
    }

    // 4. Delete Company Info
    @Transactional
    public ResponseEntity<GlobalResponseDto> deleteCompany(Long id, Admin admin) {

        // Get Company By id
        Optional<Company> company = companyRepository.findById(id);

        // Company Validation
        if (company.isEmpty()) {
            throw new CompanyException(ErrorType.COMPANY_NOT_FOUND);
        }

        // CompanyRepo Delete
        companyRepository.deleteById(id);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.COMPANY_DELETE_SUCCESS));
    }

    // Method : Create Company Token
    public String createCompanyToken() {

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[ID_LENGTH];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (byte b : bytes) {
            sb.append(BASE62.charAt(Math.abs(b) % BASE62.length()));
        }
        return sb.toString();
    }

}
