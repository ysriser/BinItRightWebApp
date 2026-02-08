package tech3.binitright.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "userUachievements")
public final class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userUachievementUid")
    private Long userAchievementId;

    @ManyToOne
    @JoinColumn(name = "userUid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievementUid")
    private Achievement achievement;

    public UserAchievement() {
    }

    public UserAchievement(final Long userAchievementId, final User user, final Achievement achievement) {
        super();
        this.userAchievementId = userAchievementId;
        this.user = user;
        this.achievement = achievement;
    }

    public Long getUserAchievementId() {
        return userAchievementId;
    }

    public void setUserAchievementId(final Long userAchievementId) {
        this.userAchievementId = userAchievementId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(final Achievement achievement) {
        this.achievement = achievement;
    }
}