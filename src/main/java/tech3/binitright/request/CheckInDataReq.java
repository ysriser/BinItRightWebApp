package tech3.binitright.request;

import java.time.LocalDateTime;

import tech3.binitright.model.CheckIn;

public final class CheckInDataReq {
    private Long userId;
    private String binId;
    private String wasteCategory;
    private LocalDateTime checkInTime;
    private CheckIn.Status status;
    private Integer quantity;
    private Integer rewardPoints;
    private Long duration;
    private String videoKey;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(final String binId) {
        this.binId = binId;
    }

    public String getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategory(final String wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(final LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public CheckIn.Status getStatus() {
        return status;
    }

    public void setStatus(final CheckIn.Status status) {
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

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(final String videoKey) {
        this.videoKey = videoKey;
    }

    @Override
    public String toString() {
        return "CheckInDataReq{"
                + "userId=" + userId
                + ", binId=" + binId
                + ", wasteCategory='" + wasteCategory + '\''
                + ", checkInTime=" + checkInTime
                + ", status=" + status
                + ", quantity=" + quantity
                + ", rewardPoints=" + rewardPoints
                + ", duration=" + duration
                + ", videoKey='" + videoKey + '\''
                + '}';
    }
}