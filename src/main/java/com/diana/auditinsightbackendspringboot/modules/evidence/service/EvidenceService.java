package com.diana.auditinsightbackendspringboot.modules.evidence.service;

import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceRequest;
import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceResponse;
import com.diana.auditinsightbackendspringboot.modules.evidence.entity.Evidence;
import com.diana.auditinsightbackendspringboot.modules.evidence.mapper.EvidenceMapper;
import com.diana.auditinsightbackendspringboot.modules.evidence.repository.EvidenceRepository;
import com.diana.auditinsightbackendspringboot.modules.transactions.entity.Transaction;
import com.diana.auditinsightbackendspringboot.modules.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EvidenceService {

    private final EvidenceRepository repository;
    private final TransactionRepository transactionRepository;

    public EvidenceService(
            EvidenceRepository repository,
            TransactionRepository transactionRepository
    ) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    public List<EvidenceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(EvidenceMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EvidenceResponse getById(Long id) {
        Evidence e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));

        return EvidenceMapper.toResponse(e);
    }

    public EvidenceResponse create(EvidenceRequest request) {

        Transaction transaction = null;

        if (request.getTransactionId() != null) {
            transaction = transactionRepository.findById(request.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
        }

        Evidence saved = repository.save(
                EvidenceMapper.toEntity(request, transaction)
        );

        return EvidenceMapper.toResponse(saved);
    }

    // ✅ FILE UPLOAD WITH VALIDATION
    public EvidenceResponse uploadEvidence(
            MultipartFile file,
            Long transactionId,
            String name,
            String category,
            String subCategory,
            String notes,
            java.math.BigDecimal amount,
            String counterpartyName
    ) {

        // ✅ FILE TYPE VALIDATION
        String contentType = file.getContentType();

        List<String> allowedTypes = List.of(
                "application/pdf",
                "image/png",
                "image/jpeg",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new RuntimeException("Invalid file type");
        }

        try {

            Transaction transaction = transactionRepository
                    .findById(transactionId)
                    .orElseThrow(() ->
                            new RuntimeException("Transaction not found"));

            // ✅ CREATE UPLOADS FOLDER
            String uploadDir =
                    System.getProperty("user.home")
                            + "/auditinsight-uploads/";

            Files.createDirectories(Paths.get(uploadDir));

            // ✅ UNIQUE FILE NAME
            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // ✅ SAVE EVIDENCE
            Evidence evidence = Evidence.builder()

                    // DOCUMENT
                    .name(
                            name != null && !name.isBlank()
                                    ? name
                                    : file.getOriginalFilename()
                    )

                    .category(category)
                    .subCategory(subCategory)

                    // FILE
                    .type(contentType)

                    .url(
                            "http://localhost:8080/uploads/"
                                    + fileName
                    )

                    // META
                    .uploadedAt(LocalDateTime.now())
                    .date(java.time.LocalDate.now())
                    .status("Pending")
                    .notes(notes)

                    // RELATION
                    .transaction(transaction)

                    // FINANCIALS
                    .amount(amount)
                    .counterpartyName(counterpartyName)

                    .build();

            Evidence saved = repository.save(evidence);

            return EvidenceMapper.toResponse(saved);

        } catch (IOException e) {

            throw new RuntimeException(
                    "File upload failed: " + e.getMessage()
            );
        }
    }

    public List<EvidenceResponse> getByTransaction(Long transactionId) {
        return repository.findByTransaction_Id(transactionId)
                .stream()
                .map(EvidenceMapper::toResponse)
                .collect(Collectors.toList());
    }
}