package tech3.binitright.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class Report {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportUid")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Admin admin;
   
    @Column(name = "generatedUat")
    private LocalDateTime generatedAt;
    
    public Report() {}

	public Report(Long reportId, Admin admin, LocalDateTime generatedAt) {
		super();
		this.reportId = reportId;
		this.admin = admin;
		this.generatedAt = generatedAt;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportIdId) {
		this.reportId = reportId;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}



}
