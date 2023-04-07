package com.solver.solver_be.domain.visitform.repository;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitFormRepository extends JpaRepository<VisitForm, Long>, CustomVisitFormRepository {

    List<VisitForm> findByGuestId(Long guestId);

    List<VisitForm> findByAdminId(Long adminId);

    VisitForm findByIdAndAdminId(Long id, Long adminId);

    Optional<VisitForm> findByAdminIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Long id, LocalDateTime startTime, LocalDateTime endTime);

    List<VisitForm> findAllByOrderByGuestNameDesc();

    List<VisitForm> findAllByOrderByLocationAsc();

    List<VisitForm> findAllByOrderByAdminNameAsc();

    List<VisitForm> findAllByOrderByStartDateDesc();

    List<VisitForm> findAllByOrderByEndDateAsc();

    List<VisitForm> findAllByOrderByPurposeAsc();

    List<VisitForm> findAllByOrderByStatusDesc();

    List<VisitForm> findAllByAdmin(Admin admin);

    List<VisitForm> findByGuestAndStartTimeBetweenAndAdminCompany(Guest guest, LocalDateTime startTimeBeforeOneHour, LocalDateTime startTimeAfterOneHour, Company company);

    List<VisitForm> findByAdminIdOrderByGuestNameDesc(Long id);

    List<VisitForm> findByAdminIdOrderByLocationAsc(Long id);

    List<VisitForm> findByAdminIdOrderByAdminNameAsc(Long id);

    List<VisitForm> findByAdminIdOrderByStartDateAsc(Long id);

    List<VisitForm> findByAdminIdOrderByEndDateAsc(Long id);

    List<VisitForm> findByAdminIdOrderByPurposeAsc(Long id);

    List<VisitForm> findByAdminIdOrderByStatusAsc(Long id);

    List<VisitForm> findByAdminIdOrderByGuestNameAsc(Long id);

    List<VisitForm> findByAdminIdOrderByLocationDesc(Long id);

    List<VisitForm> findByAdminIdOrderByAdminNameDesc(Long id);

    List<VisitForm> findByAdminIdOrderByStartDateDesc(Long id);

    List<VisitForm> findByAdminIdOrderByEndDateDesc(Long id);

    List<VisitForm> findByAdminIdOrderByPurposeDesc(Long id);

    List<VisitForm> findByAdminIdOrderByStatusDesc(Long id);
}
