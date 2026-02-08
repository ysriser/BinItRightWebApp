package tech3.binitright.model;

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
    @Column(name = "rewardUid")
    private Long rewardId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "pointsUrequired")
    private Integer pointsRequired;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RewardStatus status;

    public Reward() {}
    public Reward(final Long rewardId, final String name, final String description, final Integer pointsRequired, final Integer stock,
			final RewardStatus status, final List<RewardRedemption> rewardRedemption) {
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
    public void setRewardId(final Long rewardId) {
        this.rewardId = rewardId;
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getPointsRequired() {
        return pointsRequired;
    }
    public void setPointsRequired(final Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public Integer getStock() {
        return stock;
    }
    public void setStock(final Integer stock) {
        this.stock = stock;
    }

    public RewardStatus getStatus() {
        return status;
    }
    public void setStatus(final RewardStatus status) {
        this.status = status;
    }

    public List<RewardRedemption> getRewardRedemption() {
        return rewardRedemption;
    }
    public void setRewardRedemption(final List<RewardRedemption> rewardRedemption) {
        this.rewardRedemption = rewardRedemption;
    }
}