package com.diana.auditinsightbackendspringboot.modules.transactions.controller;

import com.diana.auditinsightbackendspringboot.modules.transactions.dto.TransactionRequest;
import com.diana.auditinsightbackendspringboot.modules.transactions.dto.TransactionResponse;
import com.diana.auditinsightbackendspringboot.modules.transactions.dto.TransactionStatusPatchRequest;
import com.diana.auditinsightbackendspringboot.modules.transactions.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // 🟢 GET ALL
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // 🟢 GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // 🟢 CREATE
    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @RequestBody TransactionRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @RequestBody TransactionRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TransactionResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody TransactionStatusPatchRequest request
    ) {
        return ResponseEntity.ok(
                service.updateStatus(id, request.getStatus())
        );
    }
}