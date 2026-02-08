package tech3.binitright.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "appUusers")
public class User extends BinItRightUser{
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

    @OneToMany(mappedBy="user")
    private List<CheckIn> checkin;

    @OneToMany(mappedBy="user")
    private List<RewardRedemption> rewardredemption;

    @OneToMany(mappedBy="user")
    private List<Feedback> Feedbacks;

    public User() {}

    public User(String userAddress, int currentRank,float carbonEmissionSaved) {
        this.userAddress = userAddress;
        this.currentRank=currentRank;
        this.carbonEmissionSaved=carbonEmissionSaved;
        ;
    }

    @PrePersist
    protected void onCreate() {
        this.updatedat = LocalDateTime.now();
    }

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public int getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}

	public float getCarbonEmissionSaved() {
		return carbonEmissionSaved;
	}

	public void setCarbonEmissionSaved(float carbonEmissionSaved) {
		this.carbonEmissionSaved = carbonEmissionSaved;
	}

	public LocalDateTime getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(LocalDateTime updatedat) {
		this.updatedat = updatedat;
	}

    public Integer getPointBalance() { return pointBalance; }

    public void setPointBalance(Integer pointBalance) { this.pointBalance = pointBalance; }

	public List<UserAccessories> getUserAccessories() {
		return userAccessories;
	}

	public void setUserAccessories(List<UserAccessories> userAccessories) {
		this.userAccessories = userAccessories;
	}

	public List<UserAchievement> getUserAchievements() {
		return userAchievements;
	}

	public void setUserAchievements(List<UserAchievement> userAchievements) {
		this.userAchievements = userAchievements;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public List<CheckIn> getCheckin() {
		return checkin;
	}

	public void setCheckin(List<CheckIn> checkin) {
		this.checkin = checkin;
	}

	public List<RewardRedemption> getRewardredemption() {
		return rewardredemption;
	}

	public void setRewardredemption(List<RewardRedemption> rewardredemption) {
		this.rewardredemption = rewardredemption;
	}

	public List<Feedback> getFeedbacks() {
		return Feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		Feedbacks = feedbacks;
	}

}