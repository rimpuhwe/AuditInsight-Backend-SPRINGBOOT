package com.diana.auditinsightbackendspringboot.modules.transactions.mapper;

public class TransactionMapper {

    // DTO → ENTITY
    public static Transaction toEntity(TransactionRequest dto) {
        Transaction t = new Transaction();
        t.setDate(dto.getDate());
        t.setAmount(dto.getAmount());
        t.setCounterparty(dto.getCounterparty());
        t.setType(dto.getType());
        t.setSource(dto.getSource());
        t.setStatus(dto.getStatus());
        t.setRiskScore(dto.getRiskScore());
        return t;
    }

    // ENTITY → DTO
    public static TransactionResponse toResponse(Transaction t) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(t.getId());
        dto.setDate(t.getDate());
        dto.setAmount(t.getAmount());
        dto.setCounterparty(t.getCounterparty());
        dto.setType(t.getType());
        dto.setSource(t.getSource());
        dto.setStatus(t.getStatus());
        dto.setRiskScore(t.getRiskScore());
        return dto;
    }

}
