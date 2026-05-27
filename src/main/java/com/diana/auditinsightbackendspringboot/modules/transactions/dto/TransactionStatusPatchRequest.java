package com.diana.auditinsightbackendspringboot.modules.transactions.dto;

import lombok.Data;

@Data
public class TransactionStatusPatchRequest {

    /** Allowed values when current status is pending: completed, flagged */
    private String status;
}
