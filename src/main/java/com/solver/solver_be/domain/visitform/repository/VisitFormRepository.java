package com.solver.solver_be.domain.visitform.repository;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitFormRepository extends JpaRepository<VisitForm, Long>, CustomVisitFormRepository {

    List<VisitForm> findByGuestId(Long guestId);

    List<VisitForm> findByAdminId(Long adminId);

    VisitForm findByIdAndAdminId(Long id, Long adminId);

    VisitForm findByGuestAndStartDateAndAdmin(Guest guest, LocalDate now, Admin admin);
}
