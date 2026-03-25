package com.diana.auditinsightbackendspringboot.modules.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {

    @Email
    private String email;
}
