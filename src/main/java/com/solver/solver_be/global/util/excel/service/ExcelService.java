package com.solver.solver_be.global.util.excel.service;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import com.solver.solver_be.domain.visitform.repository.VisitFormRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExcelService {

    private final VisitFormRepository visitFormRepository;

    @Transactional
    public Object getVisitFormsExcel(HttpServletResponse response, Admin admin) {

        List<VisitForm> visitFormList = visitFormRepository.findAllByAdmin(admin);
        createExcelDownloadResponse(response, visitFormList);
        return null;
    }

    private void createExcelDownloadResponse(HttpServletResponse response, List<VisitForm> visitFormList) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("출입신청 내역");

            // 1. FileName
            final String fileName = "출입신청 내역";

            // 2. Header
            final String[] header = {"번호", "방문자명", "방문장소", "찾아갈분", "목적", "방문기간", "상태"};

            Row row = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header[i]);
            }

            // 3. Body
            for (int i = 0; i < visitFormList.size(); i++) {
                row = sheet.createRow(i + 1);  //헤더 이후로 데이터가 출력되어야하니 +1

                VisitForm visitForm = visitFormList.get(i);
                String timeLine = String.valueOf(visitForm.getStartTime()) + " ~ " + String.valueOf(visitForm.getEndTime());

                Cell cell = null;
                cell = row.createCell(0);
                cell.setCellValue(visitForm.getId());

                cell = row.createCell(1);
                cell.setCellValue(visitForm.getGuest().getName());

                cell = row.createCell(2);
                cell.setCellValue(visitForm.getLocation());

                cell = row.createCell(3);
                cell.setCellValue(visitForm.getAdmin().getName());

                cell = row.createCell(4);
                cell.setCellValue(visitForm.getPurpose());

                cell = row.createCell(5);
                cell.setCellValue(timeLine);

                cell = row.createCell(6);
                cell.setCellValue(visitForm.getStatus());
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
