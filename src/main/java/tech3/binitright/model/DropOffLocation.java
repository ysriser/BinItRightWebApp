package tech3.binitright.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "dropUoffUlocation")
public final class DropOffLocation {

    @Id
    @Column(name = "dropUoffUid", length = 32, nullable = false, updatable = false)
    private String id;

    private String name;
    private String address;

    @Column(name = "postalUcode")
    private String postalCode;

    private String description;

    @Column(name = "binUtype")
    private String binType;

    @Column(precision = 10, scale = 4)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 4)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "dropOffLocation", cascade = CascadeType.ALL)
    private List<CheckIn> checkIn = new ArrayList<>();

    public enum Status {
        OPEN, CLOSED, MAINTENANCE, ACTIVE
    }

    public DropOffLocation() {
    }

    public DropOffLocation(final String id, final String name, final String address,
                           final String postalCode, final String description,
                           final String binType, final BigDecimal latitude, final BigDecimal longitude,
                           final Status status, final List<CheckIn> checkIn) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.description = description;
        this.binType = binType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.checkIn = checkIn;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(final String binType) {
        this.binType = binType;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public List<CheckIn> getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(final List<CheckIn> checkIn) {
        this.checkIn = checkIn;
    }
}