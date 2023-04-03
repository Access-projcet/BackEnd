package com.solver.solver_be.domain.visitform.repository;

import com.solver.solver_be.domain.visitform.entity.VisitForm;

import java.util.List;

public interface CustomVisitFormRepository {

    List<VisitForm> findByGuestNameOrLocationOrAdminNameOrStartDateOrEndDateOrPurposeAndStatus(String guestName, String location, String adminName, String startDate, String endDate, String purpose, String status);
}
