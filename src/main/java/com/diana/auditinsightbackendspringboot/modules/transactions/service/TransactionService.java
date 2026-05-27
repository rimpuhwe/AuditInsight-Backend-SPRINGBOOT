package com.diana.auditinsightbackendspringboot.modules.transactions.service;

import com.diana.auditinsightbackendspringboot.modules.evidence.entity.Evidence;
import com.diana.auditinsightbackendspringboot.modules.evidence.repository.EvidenceRepository;
import com.diana.auditinsightbackendspringboot.modules.transactions.dto.TransactionRequest;
import com.diana.auditinsightbackendspringboot.modules.transactions.dto.TransactionResponse;
import com.diana.auditinsightbackendspringboot.modules.transactions.entity.Transaction;
import com.diana.auditinsightbackendspringboot.modules.transactions.mapper.TransactionMapper;
import com.diana.auditinsightbackendspringboot.modules.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Set<String> ALLOWED_STATUS_TRANSITIONS =
            Set.of("completed", "flagged");

    private final TransactionRepository repository;
    private final EvidenceRepository evidenceRepository;

    public TransactionService(
            TransactionRepository repository,
            EvidenceRepository evidenceRepository
    ) {
        this.repository = repository;
        this.evidenceRepository = evidenceRepository;
    }

    public List<TransactionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(TransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getById(Long id) {
        Transaction transaction = findTransactionOrThrow(id);
        return TransactionMapper.toResponse(transaction);
    }

    public TransactionResponse create(TransactionRequest request) {
        Transaction transaction = TransactionMapper.toEntity(request);
        Transaction saved = repository.save(transaction);
        return TransactionMapper.toResponse(saved);
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = findTransactionOrThrow(id);
        TransactionMapper.updateEntity(transaction, request);
        Transaction saved = repository.save(transaction);
        return TransactionMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        findTransactionOrThrow(id);

        List<Evidence> linkedEvidence =
                evidenceRepository.findByTransaction_Id(id);

        for (Evidence evidence : linkedEvidence) {
            deleteUploadedFile(evidence.getUrl());
        }

        evidenceRepository.deleteByTransaction_Id(id);
        repository.deleteById(id);
    }

    public TransactionResponse updateStatus(Long id, String newStatus) {
        Transaction transaction = findTransactionOrThrow(id);

        if (!"pending".equalsIgnoreCase(transaction.getStatus())) {
            throw new RuntimeException(
                    "Only transactions with status 'pending' can be updated"
            );
        }

        if (newStatus == null || newStatus.isBlank()) {
            throw new RuntimeException("Status is required");
        }

        String normalized = newStatus.trim().toLowerCase();

        if (!ALLOWED_STATUS_TRANSITIONS.contains(normalized)) {
            throw new RuntimeException(
                    "Status must be 'completed' or 'flagged' when current status is pending"
            );
        }

        transaction.setStatus(normalized);
        Transaction saved = repository.save(transaction);
        return TransactionMapper.toResponse(saved);
    }

    private Transaction findTransactionOrThrow(Long id) {
        return repository.findById(id)
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
            // File may already be removed; do not block DB delete
        }
    }
}
