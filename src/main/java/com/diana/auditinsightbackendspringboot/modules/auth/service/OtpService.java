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
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();
        otpRepository.save(otpEntity);
        return otp;
    }

    //Verify OTP

    public boolean verifyOtp(Long userId, String code) {
        return otpRepository.findByUserIdAndCode(userId, code)
                .map(otp -> {
                    if (otp.getUsed() || otp.getExpiryTime().isBefore(LocalDateTime.now())) {
                        return false;
                    }
                    otp.setUsed(true);
                    otpRepository.save(otp);
                    return true;
                })
                .orElse(false);
    }
}
