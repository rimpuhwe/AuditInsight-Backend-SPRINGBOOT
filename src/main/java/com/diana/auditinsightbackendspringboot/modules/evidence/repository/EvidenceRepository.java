package com.diana.auditinsightbackendspringboot.modules.evidence.repository;

import com.diana.auditinsightbackendspringboot.modules.evidence.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByTransaction_Id(Long transactionId);

    void deleteByTransaction_Id(Long transactionId);
}