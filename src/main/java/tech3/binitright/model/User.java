package tech3.binitright.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate; // 建议使用 PreUpdate 记录更新时间
import jakarta.persistence.Table;

@Entity
@Table(name = "appUusers")
public final class User extends BinItRightUser {
    @Column(name = "userUaddress")
    private String userAddress;

    @Column(name = "currentUrank")
    private Integer currentRank;

    @Column(name = "carbonUemissionUsaved")
    private float carbonEmissionSaved;

    @Column(name = "updatedUAt")
    private LocalDateTime updatedat;

    @Column(name = "pointUbalance")
    private Integer pointBalance = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAccessories> userAccessories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAchievement> userAchievements;

    @OneToMany(mappedBy = "raisedBy", cascade = CascadeType.ALL)
    private List<Issue> issues;

    @OneToMany(mappedBy = "user")
    private List<CheckIn> checkin;

    @OneToMany(mappedBy = "user")
    private List<RewardRedemption> rewardredemption;

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks;

    public User() {
    }

    public User(final String userAddress, final int currentRank, final float carbonEmissionSaved) {
        this.userAddress = userAddress;
        this.currentRank = currentRank;
        this.carbonEmissionSaved = carbonEmissionSaved;
    }

    @PreUpdate // 修复：使用不同的持久化回调，避免与基类 final 方法冲突
    protected void onUpdate() {
        this.updatedat = LocalDateTime.now();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(final String userAddress) {
        this.userAddress = userAddress;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(final int currentRank) {
        this.currentRank = currentRank;
    }

    public float getCarbonEmissionSaved() {
        return carbonEmissionSaved;
    }

    public void setCarbonEmissionSaved(final float carbonEmissionSaved) {
        this.carbonEmissionSaved = carbonEmissionSaved;
    }

    public LocalDateTime getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(final LocalDateTime updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(final Integer pointBalance) {
        this.pointBalance = pointBalance;
    }

    public List<UserAccessories> getUserAccessories() {
        return userAccessories;
    }

    public void setUserAccessories(final List<UserAccessories> userAccessories) {
        this.userAccessories = userAccessories;
    }

    public List<UserAchievement> getUserAchievements() {
        return userAchievements;
    }

    public void setUserAchievements(final List<UserAchievement> userAchievements) {
        this.userAchievements = userAchievements;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(final List<Issue> issues) {
        this.issues = issues;
    }

    public List<CheckIn> getCheckin() {
        return checkin;
    }

    public void setCheckin(final List<CheckIn> checkin) {
        this.checkin = checkin;
    }

    public List<RewardRedemption> getRewardredemption() {
        return rewardredemption;
    }

    public void setRewardredemption(final List<RewardRedemption> rewardredemption) {
        this.rewardredemption = rewardredemption;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(final List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}