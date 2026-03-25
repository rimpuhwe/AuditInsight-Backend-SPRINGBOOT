package com.diana.auditinsightbackendspringboot.modules.auth.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByUserIdAndCode(Long userId, String code);
}
