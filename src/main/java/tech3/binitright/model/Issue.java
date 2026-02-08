package tech3.binitright.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public final class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issueUid")
    private Long issueId;

    public enum IssueCategory {
        BinIssues, AppProblems, LocationErrors, Others
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "issueUcategory", nullable = false)
    private IssueCategory issueCategory;

    private String description;

    @Column(name = "createdUat")
    private LocalDateTime createdAt;

    @Column(name = "resolvedUat")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == IssueStatus.RESOLVED) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    public enum IssueStatus {
        NEW, INUPROGRESS, RESOLVED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IssueStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raisedUbyUuserUid", nullable = false)
    private User raisedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolvedUbyUadminUid")
    private Admin resolvedBy;

    public Issue() {}

    public Issue(final IssueCategory issueCategory, final String description, 
                 final IssueStatus status, final User raisedBy, final Admin resolvedBy) {
        super();
        this.issueCategory = issueCategory;
        this.description = description;
        this.status = status;
        this.raisedBy = raisedBy;
        this.resolvedBy = resolvedBy;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(final Long issueId) {
        this.issueId = issueId;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(final IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(final LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(final IssueStatus status) {
        this.status = status;
    }

    public User getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(final User raisedBy) {
        this.raisedBy = raisedBy;
    }

    public Admin getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(final Admin resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}