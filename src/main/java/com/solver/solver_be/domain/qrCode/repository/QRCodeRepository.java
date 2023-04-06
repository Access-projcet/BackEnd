package com.solver.solver_be.domain.qrCode.repository;

import com.solver.solver_be.domain.qrCode.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
}
