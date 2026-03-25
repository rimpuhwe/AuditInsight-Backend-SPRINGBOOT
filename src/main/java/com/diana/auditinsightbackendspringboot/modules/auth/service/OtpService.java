package com.diana.auditinsightbackendspringboot.modules.auth.service;

import com.diana.auditinsightbackendspringboot.modules.auth.otp.OtpEntity;
import com.diana.auditinsightbackendspringboot.modules.auth.otp.OtpRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    //Generate OTP FOR A USER
    public String generateOTP(Long userId){
        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpEntity otpEntity = OtpEntity.builder()
                .userId(userId)
                .code(otp)

    }
}
