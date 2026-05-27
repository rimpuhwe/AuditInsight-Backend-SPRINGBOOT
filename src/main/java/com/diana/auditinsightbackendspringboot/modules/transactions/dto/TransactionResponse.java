package com.diana.auditinsightbackendspringboot.modules.transactions.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;

    private LocalDate date;

    private Double amount;

    private String counterparty;

    private String type;

    private String source;

    private String status;

    private Integer riskScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}