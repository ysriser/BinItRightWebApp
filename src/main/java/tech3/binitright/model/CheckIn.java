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
@Table(name = "checkUin")
public class CheckIn {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkinUid")
    private Long checkInId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropUoffUid", nullable = false)
    private DropOffLocation dropOffLocation;

    @ManyToOne
    private WasteCategories wasteCategories;


    @Column(name = "fileUname")
    private String fileName;

    @Column(name = "checkinUtime")
    private LocalDateTime checkInTime;

    @PrePersist
    protected void onCreate() {
        this.checkInTime = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private Integer quantity;

    @Column(name = "rewardUpoints")
    private Integer rewardPoints;

    private Long duration;

    public enum Status {
        PROCESSING,
        APPROVED,
        DENIED
    }

    public CheckIn() {}

	public CheckIn(final Long checkInId, final User user, final DropOffLocation dropOffLocation,
			final WasteCategories wasteCategories,
			final String fileName, final LocalDateTime checkInTime, final Status status,
			final Integer quantity, final Integer rewardPoints,
			final Long duration) {

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

	public void setCheckInId(final Long checkInId) {
		this.checkInId = checkInId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public DropOffLocation getDropOffLocation() {
		return dropOffLocation;
	}

	public void setDropOffLocation(final DropOffLocation dropOffLocation) {
		this.dropOffLocation = dropOffLocation;
	}

	public WasteCategories getWasteCategories() {
		return wasteCategories;
	}

	public void setWasteCategories(final WasteCategories wasteCategories) {
		this.wasteCategories = wasteCategories;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(final LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(final Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(final Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}




}