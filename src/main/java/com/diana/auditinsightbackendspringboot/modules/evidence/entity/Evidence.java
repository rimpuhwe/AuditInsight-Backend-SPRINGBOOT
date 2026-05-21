package com.diana.auditinsightbackendspringboot.modules.evidence.entity;
import com.diana.auditinsightbackendspringboot.modules.transactions.entity.Transaction;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(length = 2000)
    private String notes;

    /* =========================
       TRANSACTION RELATION
    ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;



    /* =========================
       EXTRA CONTEXT
    ========================= */

    private BigDecimal amount;

    private String counterpartyName;
}