package techthree.binitright.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Achievement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Long achievementId;

    private String name;
    private String description;
    private String criteria;

    @Column(name = "badge_icon_url")
    private String badgeIconUrl;

    @OneToMany(mappedBy="achievement", cascade=CascadeType.ALL)
    private List<UserAchievement> userAchievement;

    public Achievement() {}

    public Achievement(String name, String description, String criteria, String badgeIconUrl) {
        this.name = name;
        this.description = description;
        this.criteria = criteria;
        this.badgeIconUrl = badgeIconUrl;
    }

    // Getters and Setters
    public Long getAchievementId() { return achievementId; }
    public void setAchievementId(Long achievementId) { this.achievementId = achievementId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCriteria() { return criteria; }
    public void setCriteria(String criteria) { this.criteria = criteria; }

    public String getBadgeIconUrl() { return badgeIconUrl; }
    public void setBadgeIconUrl(String badgeIconUrl) { this.badgeIconUrl = badgeIconUrl; }

    public List<UserAchievement> getUserAchievement() { return userAchievement; }
    public void setUserAchievement(List<UserAchievement> userAchievement) { this.userAchievement = userAchievement; }
}