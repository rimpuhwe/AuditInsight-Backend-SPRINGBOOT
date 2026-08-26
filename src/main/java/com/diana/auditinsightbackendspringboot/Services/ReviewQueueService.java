package com.diana.auditinsightbackendspringboot.Services;

import com.diana.auditinsightbackendspringboot.DTOs.CreateReviewQueueRequest;
import com.diana.auditinsightbackendspringboot.DTOs.ResolveReviewQueueRequest;
import com.diana.auditinsightbackendspringboot.DTOs.ReviewQueueResponse;
import com.diana.auditinsightbackendspringboot.Enum.Permission;
import com.diana.auditinsightbackendspringboot.Enum.ReviewStatus;
import com.diana.auditinsightbackendspringboot.Exceptions.Custom.ForbiddenException;
import com.diana.auditinsightbackendspringboot.Exceptions.Custom.InvalidRecord;
import com.diana.auditinsightbackendspringboot.Models.ReviewQueue;
import com.diana.auditinsightbackendspringboot.Repositories.*;
import com.diana.auditinsightbackendspringboot.Services.OrgAccessService.OrgMemberContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewQueueService {

    private final ReviewQueueRepository reviewRepo;
    private final TransactionRepository txnRepo;
    private final OrgAccessService orgAccessService;
    private final NotificationService notificationService;

    public ReviewQueueService(ReviewQueueRepository reviewRepo,
                              TransactionRepository txnRepo,
                              OrgAccessService orgAccessService,
                              NotificationService notificationService) {
        this.reviewRepo = reviewRepo;
        this.txnRepo = txnRepo;
        this.orgAccessService = orgAccessService;
        this.notificationService = notificationService;
    }


    private Mono<OrgMemberContext> resolveContext(UUID orgId, String email) {
        return orgAccessService.resolveGatedMember(orgId, email);
    }


    private ReviewQueueResponse toResponse(ReviewQueue rq) {
        ReviewQueueResponse r = new ReviewQueueResponse();
        r.setId(rq.getId());
        r.setOrganisationId(rq.getOrganisationId());
        r.setTransactionId(rq.getTransactionId());
        r.setIssueType(rq.getIssueType());
        r.setDescription(rq.getDescription());
        r.setStatus(rq.getStatus());
        r.setFlaggedBy(rq.getFlaggedBy());
        r.setResolvedBy(rq.getResolvedBy());
        r.setResolutionNote(rq.getResolutionNote());
        r.setCreatedAt(rq.getCreatedAt());
        r.setResolvedAt(rq.getResolvedAt());
        return r;
    }


    public Mono<ReviewQueueResponse> flagIssue(String email, CreateReviewQueueRequest req) {
        return resolveContext(req.getOrganisationId(), email)
                .flatMap(ctx -> {
                    if (!orgAccessService.hasPermission(ctx.role(), Permission.REVIEW_FLAG)) {
                        return Mono.error(new ForbiddenException(
                                "Permission denied. Only auditors can flag issues."));
                    }
                    return txnRepo.findById(req.getTransactionId())
                            .switchIfEmpty(Mono.error(new InvalidRecord("Transaction not found")))
                            .flatMap(txn -> {
                                if (!txn.getOrganisationId().equals(req.getOrganisationId())) {
                                    return Mono.error(new InvalidRecord(
                                            "Transaction does not belong to this organisation"));
                                }
                                ReviewQueue rq = new ReviewQueue();
                                rq.setOrganisationId(req.getOrganisationId());
                                rq.setTransactionId(req.getTransactionId());
                                rq.setIssueType(req.getIssueType());
                                rq.setDescription(req.getDescription());
                                rq.setStatus(ReviewStatus.OPEN);
                                rq.setFlaggedBy(String.valueOf(ctx.user().getId()));
                                rq.setCreatedAt(LocalDateTime.now());
                                return reviewRepo.save(rq)
                                        .flatMap(saved -> notificationService
                                                .notifyIssueFlagged(
                                                        saved.getOrganisationId(),
                                                        saved.getTransactionId(),
                                                        saved.getIssueType().name(),
                                                        saved.getDescription(),
                                                        ctx.user().getFullName())
                                                .thenReturn(toResponse(saved)));
                            });
                });
    }


    public Flux<ReviewQueueResponse> listByOrg(UUID orgId, String email) {
        return resolveContext(orgId, email)
                .thenMany(reviewRepo.findAllByOrganisationId(orgId))
                .map(this::toResponse);
    }


    public Mono<ReviewQueueResponse> getItem(UUID itemId, String email) {
        return reviewRepo.findById(itemId)
                .switchIfEmpty(Mono.error(new InvalidRecord("Review queue item not found")))
                .flatMap(rq -> resolveContext(rq.getOrganisationId(), email)
                        .thenReturn(toResponse(rq)));
    }

    public Mono<ReviewQueueResponse> resolveIssue(UUID itemId, String email,
                                                  ResolveReviewQueueRequest req) {
        return reviewRepo.findById(itemId)
                .switchIfEmpty(Mono.error(new InvalidRecord("Review queue item not found")))
                .flatMap(rq -> resolveContext(rq.getOrganisationId(), email)
                        .flatMap(ctx -> {
                            if (!orgAccessService.hasPermission(ctx.role(), Permission.REVIEW_RESOLVE)) {
                                return Mono.error(new ForbiddenException(
                                        "Permission denied. Auditors cannot resolve issues."));
                            }
                            if (rq.getStatus() == ReviewStatus.RESOLVED) {
                                return Mono.error(new InvalidRecord(
                                        "This issue has already been resolved."));
                            }
                            rq.setStatus(ReviewStatus.RESOLVED);
                            rq.setResolvedBy(ctx.user().getId());
                            rq.setResolutionNote(req.getResolutionNote());
                            rq.setResolvedAt(LocalDateTime.now());
                            return reviewRepo.save(rq)
                                    .flatMap(saved -> notificationService
                                            .notifyIssueResolved(
                                                    saved.getOrganisationId(),
                                                    saved.getTransactionId(),
                                                    saved.getResolutionNote(),
                                                    ctx.user().getFullName())
                                            .thenReturn(toResponse(saved)));
                        }));
    }
}
