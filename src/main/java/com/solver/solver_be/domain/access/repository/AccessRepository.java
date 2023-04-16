package com.solver.solver_be.domain.access.repository;

import com.solver.solver_be.domain.access.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {

    Optional<Access> findByVisitFormId(Long id);

    List<Access> findByAdminId(Long id);

    Optional<Access> findLatestByGuestNameAndGuestPhoneNum(String name, String phoneNum);

}
