package com.solver.solver_be.domain.visitform.repository;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitFormRepository extends JpaRepository<VisitForm, Long>, CustomVisitFormRepository {

    List<VisitForm> findByGuestId(Long guestId);

    List<VisitForm> findByAdminId(Long adminId);

    List<VisitForm> findAllByAdmin(Admin admin);

    VisitForm findByIdAndAdminId(Long id, Long adminId);

    Optional<VisitForm> findByAdminIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Long id, LocalDateTime startTime, LocalDateTime endTime);

    List<VisitForm> findByGuestAndStartTimeBetweenAndAdminCompany(Guest guest, LocalDateTime startTimeBeforeOneHour, LocalDateTime startTimeAfterOneHour, Company company);

}
