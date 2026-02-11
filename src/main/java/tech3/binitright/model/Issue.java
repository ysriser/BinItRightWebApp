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
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    public enum IssueCategory {
        BIN_ISSUES,
        APP_PROBLEMS,
        LOCATION_ERRORS,
        OTHERS
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_category", nullable = false)
    private IssueCategory issueCategory;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
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
        NEW,
        IN_PROGRESS,
        RESOLVED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IssueStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raised_by_user_id", nullable = false)
    private User raisedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_admin_id")
    private Admin resolvedBy;
    
    public Issue() {}
	public Issue( IssueCategory issueCategory, String description, 
			 IssueStatus status, User raisedBy, Admin resolvedBy) {
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

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public IssueCategory getIssueCategory() {
		return issueCategory;
	}

	public void setIssueCategory(IssueCategory issueCategory) {
		this.issueCategory = issueCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

	public User getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(User raisedBy) {
		this.raisedBy = raisedBy;
	}

	public Admin getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(Admin resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

}