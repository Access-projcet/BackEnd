package com.solver.solver_be.domain.access.repository;

import com.solver.solver_be.domain.access.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {

    Optional<Access> findByGuestName(String name);
}
