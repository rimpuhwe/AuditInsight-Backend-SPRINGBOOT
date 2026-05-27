package com.diana.auditinsightbackendspringboot.modules.evidence.controller;

import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceRequest;
import com.diana.auditinsightbackendspringboot.modules.evidence.dto.EvidenceResponse;
import com.diana.auditinsightbackendspringboot.modules.evidence.service.EvidenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/evidence")
@CrossOrigin(origins = "http://localhost:3000")
public class EvidenceController {

    private final EvidenceService service;

    public EvidenceController(EvidenceService service) {
        this.service = service;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<EvidenceResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EvidenceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ GET EVIDENCE BY TRANSACTION
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<EvidenceResponse>> getByTransaction(
            @PathVariable Long transactionId
    ) {
        return ResponseEntity.ok(
                service.getByTransaction(transactionId)
        );
    }

    // CREATE
    @PostMapping
    public ResponseEntity<EvidenceResponse> create(
            @RequestBody EvidenceRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    // ✅ FILE UPLOAD
    @PostMapping("/upload")
    public ResponseEntity<EvidenceResponse> uploadEvidence(
            @RequestParam("file") MultipartFile file,

            @RequestParam("transactionId") Long transactionId,

            @RequestParam(value = "name", required = false)
            String name,

            @RequestParam(value = "category", required = false)
            String category,

            @RequestParam(value = "subCategory", required = false)
            String subCategory,

            @RequestParam(value = "notes", required = false)
            String notes,

            @RequestParam(value = "amount", required = false)
            java.math.BigDecimal amount,

            @RequestParam(value = "counterpartyName", required = false)
            String counterpartyName
    ) {
        return ResponseEntity.ok(
                service.uploadEvidence(
                        file,
                        transactionId,
                        name,
                        category,
                        subCategory,
                        notes,
                        amount,
                        counterpartyName
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvidenceResponse> update(
            @PathVariable Long id,
            @RequestBody EvidenceRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<EvidenceResponse> verify(@PathVariable Long id) {
        return ResponseEntity.ok(service.verify(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<EvidenceResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(service.reject(id));
    }
}