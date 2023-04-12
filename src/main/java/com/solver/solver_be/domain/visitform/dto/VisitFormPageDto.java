package com.solver.solver_be.domain.visitform.dto;


import com.solver.solver_be.domain.visitform.entity.VisitForm;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class VisitFormPageDto {

    private Long totalPage;

    private List<VisitForm> visitFormList = new ArrayList<>();

    public static VisitFormPageDto of(Long totalPage, List<VisitForm> visitFormList){
        return VisitFormPageDto.builder()
                .totalPage(totalPage)
                .visitFormList(visitFormList)
                .build();
    }
}
