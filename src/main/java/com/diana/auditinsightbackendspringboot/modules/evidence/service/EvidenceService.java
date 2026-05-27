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
        return EvidenceMapper.toResponse(findEvidenceOrThrow(id));
    }

    public EvidenceResponse create(EvidenceRequest request) {
        Transaction transaction = resolveTransaction(request.getTransactionId());
        Evidence saved = repository.save(
                EvidenceMapper.toEntity(request, transaction)
        );
        return EvidenceMapper.toResponse(saved);
    }

    public EvidenceResponse update(Long id, EvidenceRequest request) {
        Evidence evidence = findEvidenceOrThrow(id);
        Transaction transaction = resolveTransaction(request.getTransactionId());
        EvidenceMapper.updateEntity(evidence, request, transaction);
        Evidence saved = repository.save(evidence);
        return EvidenceMapper.toResponse(saved);
    }

    public void delete(Long id) {
        Evidence evidence = findEvidenceOrThrow(id);
        deleteUploadedFile(evidence.getUrl());
        repository.delete(evidence);
    }

    public EvidenceResponse verify(Long id) {
        return updateReviewStatus(id, "Verified");
    }

    public EvidenceResponse reject(Long id) {
        return updateReviewStatus(id, "Rejected");
    }

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

            String uploadDir =
                    System.getProperty("user.home")
                            + "/auditinsight-uploads/";

            Files.createDirectories(Paths.get(uploadDir));

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

            Evidence evidence = Evidence.builder()
                    .name(
                            name != null && !name.isBlank()
                                    ? name
                                    : file.getOriginalFilename()
                    )
                    .category(category)
                    .subCategory(subCategory)
                    .type(contentType)
                    .url(
                            "http://localhost:8080/uploads/"
                                    + fileName
                    )
                    .uploadedAt(LocalDateTime.now())
                    .date(java.time.LocalDate.now())
                    .status("Pending")
                    .notes(notes)
                    .transaction(transaction)
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
        if (!transactionRepository.existsById(transactionId)) {
            throw new RuntimeException("Transaction not found");
        }

        return repository.findByTransaction_Id(transactionId)
                .stream()
                .map(EvidenceMapper::toResponse)
                .collect(Collectors.toList());
    }

    private EvidenceResponse updateReviewStatus(Long id, String targetStatus) {
        Evidence evidence = findEvidenceOrThrow(id);

        if (!"pending".equalsIgnoreCase(evidence.getStatus())) {
            throw new RuntimeException(
                    "Only evidence with status 'Pending' can be verified or rejected"
            );
        }

        evidence.setStatus(targetStatus);
        Evidence saved = repository.save(evidence);
        return EvidenceMapper.toResponse(saved);
    }

    private Evidence findEvidenceOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));
    }

    private Transaction resolveTransaction(Long transactionId) {
        if (transactionId == null) {
            return null;
        }

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    private void deleteUploadedFile(String url) {
        if (url == null || !url.contains("/uploads/")) {
            return;
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String uploadDir =
                System.getProperty("user.home") + "/auditinsight-uploads/";

        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception ignored) {
            // File may already be removed
        }
    }
}
