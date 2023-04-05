package com.solver.solver_be.global.util.excel.controller;

import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import com.solver.solver_be.global.util.excel.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/excel/access")
    public ResponseEntity getVisitFormsExcel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             HttpServletResponse response){
        return ResponseEntity.ok(excelService.getVisitFormsExcel(response,userDetails.getAdmin()));
    }

}
