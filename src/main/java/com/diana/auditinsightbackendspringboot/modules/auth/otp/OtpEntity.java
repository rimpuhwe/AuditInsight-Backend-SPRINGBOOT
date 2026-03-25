package com.diana.auditinsightbackendspringboot.modules.auth.otp;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;   // the OTP itself

    @Column(nullable = false)
    private Long userId;    // which user this otp belongs to.

    @Column(nullable = false)
    private LocalDateTime expiryTime;  // OTP expiration.

    @Column(nullable = false)
    private Boolean used = false;   // has it been used yet?


}
