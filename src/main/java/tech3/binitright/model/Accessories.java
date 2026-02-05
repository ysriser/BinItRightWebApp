package tech3.binitright.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accessories")
public class Accessories {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accessories_id")
    private Long accessoriesId;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "required_points")
    private int requiredPoints;

    @OneToMany(mappedBy="accessories")
    private List<UserAccessories> userAccessories;
    
    public Accessories() {}
    public Accessories(Long accesoriesId, String name, String imageUrl, int requiredPoints,
                       List<UserAccessories> userAccessories) {
        super();
        this.accessoriesId = accesoriesId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.requiredPoints = requiredPoints;
        this.userAccessories = userAccessories;
    }

    public Long getAccesoriesId() {
        return accessoriesId;
    }

    public void setAccesoriesId(Long accesoriesId) {
        this.accessoriesId = accesoriesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public List<UserAccessories> getUserAccessories() {
        return userAccessories;
    }

    public void setUserAccessories(List<UserAccessories> userAccessories) {
        this.userAccessories = userAccessories;
    }

}
