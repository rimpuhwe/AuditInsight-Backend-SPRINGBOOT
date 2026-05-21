package com.diana.auditinsightbackendspringboot.modules.evidence.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EvidenceResponse {

    private Long id;

    private String name;

    private String category;

    private String subCategory;

    private String type;

    private String url;

    private LocalDate date;

    private String uploadedBy;

    private LocalDateTime uploadedAt;

    private String status;

    private String notes;

    /* =========================
       🔗 TRANSACTION LINK
    ========================= */
    private Long transactionId;

    /* =========================
       💰 FINANCIAL CONTEXT
    ========================= */
    private BigDecimal amount;

    private String counterpartyName;
}