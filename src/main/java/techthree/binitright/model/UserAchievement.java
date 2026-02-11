package techthree.binitright.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_achievement_id")
    private Long userAchievementId;

    @ManyToOne
    
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;
    
    public UserAchievement() {}
    public UserAchievement(Long userAchievementId, User user, Achievement achievement) {
        super();
        this.userAchievementId = userAchievementId;
        this.user = user;
        this.achievement = achievement;
    }

    public Long getUserAchievementId() {
        return userAchievementId;
    }

    public void setUserAchievementId(Long userAchievementId) {
        this.userAchievementId = userAchievementId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }


}