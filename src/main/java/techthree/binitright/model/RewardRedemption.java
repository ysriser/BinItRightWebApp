package techthree.binitright.model;

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
@Table(name = "reward_redemption")
public class RewardRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redemption_id")
    private Long redemptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(name = "redeemed_points")
    private Integer redeemedPoints;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;
    
    
    public RewardRedemption() {}


	public RewardRedemption(Long redemptionId, User user, Reward reward, Integer redeemedPoints,
			LocalDateTime redeemedAt) {
		super();
		this.redemptionId = redemptionId;
		this.user = user;
		this.reward = reward;
		this.redeemedPoints = redeemedPoints;
		this.redeemedAt = redeemedAt;
	}


	public Long getRedemptionId() {
		return redemptionId;
	}


	public void setRedemptionId(Long redemptionId) {
		this.redemptionId = redemptionId;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Reward getReward() {
		return reward;
	}


	public void setReward(Reward reward) {
		this.reward = reward;
	}


	public Integer getRedeemedPoints() {
		return redeemedPoints;
	}


	public void setRedeemedPoints(Integer redeemedPoints) {
		this.redeemedPoints = redeemedPoints;
	}


	public LocalDateTime getRedeemedAt() {
		return redeemedAt;
	}


	public void setRedeemedAt(LocalDateTime redeemedAt) {
		this.redeemedAt = redeemedAt;
	}
    
}