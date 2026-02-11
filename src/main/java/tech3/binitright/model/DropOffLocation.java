package tech3.binitright.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "drop_off_location")
public class DropOffLocation {

    @Id
    @Column(name = "drop_off_id", length = 32, nullable = false, updatable = false)
    private String id;

    private String name;
    
    private String address;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    private String description;
    
    @Column(name = "bin_type")
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

}