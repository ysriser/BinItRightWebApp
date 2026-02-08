package tech3.binitright.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "dropUoffUlocation")
public class DropOffLocation {

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
        OPEN,
        CLOSED,
        MAINTENANCE,
        ACTIVE  
    }
    
    public DropOffLocation() {}

	public DropOffLocation(String id, String name, String address, String postalCode, String description,
			String binType, BigDecimal latitude, BigDecimal longitude, Status status, List<CheckIn> checkIn, String incCrc) {
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

    public void setId(String id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBinType() {
		return binType;
	}

	public void setBinType(String binType) {
		this.binType = binType;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<CheckIn> getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(List<CheckIn> checkIn) {
		this.checkIn = checkIn;
	}

}