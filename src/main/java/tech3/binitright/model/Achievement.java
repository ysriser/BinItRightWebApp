package tech3.binitright.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Achievement {
    
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "achievement_id")
	    private Long achievementId;

	    private String name;
	    private String description;
	    private String criteria;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "badge_icon")
	    private BadgeIcon badgeIcon;

	    public enum BadgeIcon {
	        GOLD,
	        SILVER,
	        BRONZE,
	        PLATINUM
	    }


	    @OneToMany(mappedBy="achievement",cascade=CascadeType.ALL)
	    private List<UserAchievement> userAchievement;

        public Achievement() {}
	    public Achievement(Long achievementId, String name, String description, String criteria, BadgeIcon badgeIcon,
	                       List<UserAchievement> userAchievement) {
	        super();
	        this.achievementId = achievementId;
	        this.name = name;
	        this.description = description;
	        this.criteria = criteria;
	        this.badgeIcon = badgeIcon;
	        this.userAchievement = userAchievement;
	    }


	    public Long getAchievementId() {
	        return achievementId;
	    }


	    public void setAchievementId(Long achievementId) {
	        this.achievementId = achievementId;
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


	    public String getCriteria() {
	        return criteria;
	    }


	    public void setCriteria(String criteria) {
	        this.criteria = criteria;
	    }


	    public BadgeIcon getBadgeIcon() {
	        return badgeIcon;
	    }


	    public void setBadgeIcon(BadgeIcon badgeIcon) {
	        this.badgeIcon = badgeIcon;
	    }


	    public List<UserAchievement> getUserAchievement() {
	        return userAchievement;
	    }


	    public void setUserAchievement(List<UserAchievement> userAchievement) {
	        this.userAchievement = userAchievement;
	    }


	}
