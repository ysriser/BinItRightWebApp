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
import jakarta.persistence.Table;

@Entity
@Table(name = "check_in")
public class CheckIn {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkin_id")
    private Long checkInId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drop_off_id", nullable = false)
    private DropOffLocation dropOffLocation;
    
    @ManyToOne
    private WasteCategories wasteCategories;

    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "checkin_time")
    private LocalDateTime checkInTime;
    
    @PrePersist
    protected void onCreate() {
        this.checkInTime = LocalDateTime.now();
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    private Integer quantity;
    
    @Column(name = "reward_points")
    private Integer rewardPoints;
    
    private Long duration;
    
    public enum Status {
        PROCESSING,
        APPROVED,
        DENIED
    }
    
    public CheckIn() {}

	public CheckIn(Long checkInId, User user, DropOffLocation dropOffLocation, WasteCategories wasteCategories,
			String fileName, LocalDateTime checkInTime, Status status, Integer quantity, Integer rewardPoints,
			Long duration) {
		
		this.checkInId = checkInId;
		this.user = user;
		this.dropOffLocation = dropOffLocation;
		this.wasteCategories = wasteCategories;
		this.fileName = fileName;
		this.checkInTime = checkInTime;
		this.status = status;
		this.quantity = quantity;
		this.rewardPoints = rewardPoints;
		this.duration = duration;
	}

	public Long getCheckInId() {
		return checkInId;
	}

	public void setCheckInId(Long checkInId) {
		this.checkInId = checkInId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DropOffLocation getDropOffLocation() {
		return dropOffLocation;
	}

	public void setDropOffLocation(DropOffLocation dropOffLocation) {
		this.dropOffLocation = dropOffLocation;
	}

	public WasteCategories getWasteCategories() {
		return wasteCategories;
	}

	public void setWasteCategories(WasteCategories wasteCategories) {
		this.wasteCategories = wasteCategories;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

    
	

}