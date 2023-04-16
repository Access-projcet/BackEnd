package com.solver.solver_be.domain.accessRecord.repository;

import com.solver.solver_be.domain.access.entity.Access;
import com.solver.solver_be.domain.accessRecord.entity.AccessRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessRecordRepository extends JpaRepository <AccessRecord,Long> {

    Optional<AccessRecord> findTopByAccessOrderByInTimeDesc(Access access);

    AccessRecord findFirstByAccessOrderByInTimeDesc(Access access);

}
