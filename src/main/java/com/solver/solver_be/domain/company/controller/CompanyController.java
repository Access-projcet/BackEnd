package com.solver.solver_be.domain.company.controller;

import com.solver.solver_be.domain.company.dto.CompanyRequestDto;
import com.solver.solver_be.domain.company.service.CompanyService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 1. Register Company
    @PostMapping("/company")
    public ResponseEntity<GlobalResponseDto> registerCompany(@RequestBody CompanyRequestDto companyRequestDto) {
        return companyService.registerCompany(companyRequestDto);
    }

    // 2. Get CompanyList
    @GetMapping("/company")
    public ResponseEntity<GlobalResponseDto> getCompanies(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return companyService.getCompanies(userDetails.getGuest());
    }

    // 3. Update Company
    @PutMapping("/company/{id}")
    public ResponseEntity<GlobalResponseDto> updateCompany(@PathVariable Long id,
                                                           @RequestBody CompanyRequestDto companyRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return companyService.updateCompany(id, companyRequestDto, userDetails.getAdmin());
    }

    // 4. Delete Company
    @DeleteMapping("/company/{id}")
    public ResponseEntity<GlobalResponseDto> deleteCompany(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return companyService.deleteCompany(id, userDetails.getAdmin());
    }

}
