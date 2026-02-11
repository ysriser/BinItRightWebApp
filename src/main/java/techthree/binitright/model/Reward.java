package techthree.binitright.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reward")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id")
    private Long rewardId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "points_required")
    private Integer pointsRequired;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RewardStatus status;
    
    public Reward() {}
    public Reward(Long rewardId, String name, String description, Integer pointsRequired, Integer stock,
			RewardStatus status, List<RewardRedemption> rewardRedemption) {
		super();
		this.rewardId = rewardId;
		this.name = name;
		this.description = description;
		this.pointsRequired = pointsRequired;
		this.stock = stock;
		this.status = status;
		this.rewardRedemption = rewardRedemption;
	}
	@OneToMany(mappedBy = "reward", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RewardRedemption> rewardRedemption;

    public enum RewardStatus {
        AVAILABLE,
        UNAVAILABLE
    }

    public Long getRewardId() {
        return rewardId;
    }
    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsRequired() {
        return pointsRequired;
    }
    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public RewardStatus getStatus() {
        return status;
    }
    public void setStatus(RewardStatus status) {
        this.status = status;
    }

    public List<RewardRedemption> getRewardRedemption() {
        return rewardRedemption;
    }
    public void setRewardRedemption(List<RewardRedemption> rewardRedemption) {
        this.rewardRedemption = rewardRedemption;
    }
}