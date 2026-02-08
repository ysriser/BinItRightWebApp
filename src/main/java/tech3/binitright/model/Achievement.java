package tech3.binitright.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievementUid")
    private Long achievementId;

    private String name;
    private String description;
    private String criteria;

    @Column(name = "badgeUiconUurl")
    private String badgeIconUrl;

    @OneToMany(mappedBy="achievement", cascade=CascadeType.ALL)
    private List<UserAchievement> userAchievement;

    public Achievement() {}

    public Achievement(final String name, final String description, final String criteria, final String badgeIconUrl) {
        this.name = name;
        this.description = description;
        this.criteria = criteria;
        this.badgeIconUrl = badgeIconUrl;
    }

    // Getters and Setters
    public Long getAchievementId() { return achievementId; }
    public void setAchievementId(final Long achievementId) { this.achievementId = achievementId; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(final String description) { this.description = description; }

    public String getCriteria() { return criteria; }
    public void setCriteria(final String criteria) { this.criteria = criteria; }

    public String getBadgeIconUrl() { return badgeIconUrl; }
    public void setBadgeIconUrl(final String badgeIconUrl) { this.badgeIconUrl = badgeIconUrl; }

    public List<UserAchievement> getUserAchievement() { return userAchievement; }
    public void setUserAchievement(final List<UserAchievement> userAchievement) { this.userAchievement = userAchievement; }
}