package com.diana.auditinsightbackendspringboot.modules.evidence.mapper;

import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceRequest;
import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceResponse;
import com.diana.auditinsightbackendspringboot.modules.evidence.entity.Evidence;
import com.diana.auditinsightbackendspringboot.modules.transactions.entity.Transaction;

import java.time.LocalDateTime;

public class EvidenceMapper {

    /* REQUEST → ENTITY */
    public static Evidence toEntity(EvidenceRequest dto, Transaction transaction) {
        return Evidence.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .subCategory(dto.getSubCategory())
                .type(dto.getType())
                .url(dto.getUrl())
                .date(dto.getDate())
                .uploadedBy(dto.getUploadedBy())
                .uploadedAt(LocalDateTime.now())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .transaction(transaction)
                .amount(dto.getAmount())
                .counterpartyName(dto.getCounterpartyName())
                .build();
    }

    /* ENTITY → RESPONSE */
    public static EvidenceResponse toResponse(Evidence e) {

        EvidenceResponse dto = new EvidenceResponse();

        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setCategory(e.getCategory());
        dto.setSubCategory(e.getSubCategory());
        dto.setType(e.getType());
        dto.setUrl(e.getUrl());
        dto.setDate(e.getDate());
        dto.setUploadedBy(e.getUploadedBy());
        dto.setUploadedAt(e.getUploadedAt());
        dto.setStatus(e.getStatus());
        dto.setNotes(e.getNotes());

        // 🔗 SAFE RELATION ACCESS
        dto.setTransactionId(
                e.getTransaction() != null ? e.getTransaction().getId() : null
        );

        dto.setAmount(e.getAmount());
        dto.setCounterpartyName(e.getCounterpartyName());

        return dto;
    }
}