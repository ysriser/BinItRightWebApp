package tech3.binitright.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accessories")
public final class Accessories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accessoriesUid")
    private Long accessoriesId;

    private String name;

    @Column(name = "imageUurl")
    private String imageUrl;

    @Column(name = "requiredUpoints")
    private int requiredPoints;

    @OneToMany(mappedBy = "accessories")
    @JsonIgnore
    private List<UserAccessories> userAccessories;

    public Accessories() {
    }

    public Accessories(final Long accessoriesId, final String name, final String imageUrl, 
                       final int requiredPoints, final List<UserAccessories> userAccessories) {
        super();
        this.accessoriesId = accessoriesId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.requiredPoints = requiredPoints;
        this.userAccessories = userAccessories;
    }

    public Long getAccessoriesId() {
        return accessoriesId;
    }

    public void setAccessoriesId(final Long accessoriesId) {
        this.accessoriesId = accessoriesId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(final int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public List<UserAccessories> getUserAccessories() {
        return userAccessories;
    }

    public void setUserAccessories(final List<UserAccessories> userAccessories) {
        this.userAccessories = userAccessories;
    }
}