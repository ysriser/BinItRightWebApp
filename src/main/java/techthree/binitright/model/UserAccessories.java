package techthree.binitright.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accessories")
public class UserAccessories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_accessories_id")
    private Long userAccessoriesId;

    @Column(name = "equipped", nullable = false)
    private boolean equipped;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    
    @JoinColumn(name = "accessories_id")
    private Accessories accessories;
   
    public UserAccessories() {}
    public UserAccessories( boolean equipped, User user, Accessories accessories) {
        
        this.equipped = equipped;
        this.user = user;
        this.accessories = accessories;
    }

    public Long getUserAccessoriesId() {
        return userAccessoriesId;
    }

    public void setUserAccessoriesId(Long userAccessoriesId) {
        this.userAccessoriesId = userAccessoriesId;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Accessories getAccessories() {
        return accessories;
    }

    public void setAccessories(Accessories accessories) {
        this.accessories = accessories;
    }


}

