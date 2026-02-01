package tech3.binitright.request;

import java.math.BigDecimal;

import tech3.binitright.model.DropOffLocation.Status;

public class NearByBinDto {

        private Long id;
        private String name;
        private String address;
        private String description;
        private String postalCode;
        private String binType;
        private Double latitude;
        private Double longitude;
        private Double distanceMeters;
        private Status status;

	    public NearByBinDto() {
		
		}

		public NearByBinDto(Long dropOffId, String name, String address, String description, String postalCode,
					String binType, Status status, BigDecimal latitude, BigDecimal longitude, double distance) {
	    	 this.id = dropOffId;
	         this.name = name;
	         this.address = address;
	         this.description = description;
	         this.postalCode = postalCode;
	         this.binType = binType;
	         this.status = status;
	         this.latitude = latitude.doubleValue();
	         this.longitude = longitude.doubleValue();
	         this.distanceMeters = distanceMeters;
				
			}

			public Long getId() {
		        return id;
		    }
		
		    public String getName() {
		        return name;
		    }
		
		    public String getAddress() {
		        return address;
		    }
		
		    public String getDescription() {
		        return description;
		    }
		
		    public String getPostalCode() {
		        return postalCode;
		    }
		
		    public String getBinType() {
		        return binType;
		    }
		
		    public Double getLatitude() {
		        return latitude;
		    }
		
		    public Double getLongitude() {
		        return longitude;
		    }
		
		    public Double getDistanceMeters() {
		        return distanceMeters;
		    }
		
			public Status getStatus() {
				return status;
			}
		
			public void setStatus(Status status) {
				this.status = status;
			}
		
			public void setId(Long id) {
				this.id = id;
			}
		
			public void setName(String name) {
				this.name = name;
			}
		
			public void setAddress(String address) {
				this.address = address;
			}
		
			public void setDescription(String description) {
				this.description = description;
			}
		
			public void setPostalCode(String postalCode) {
				this.postalCode = postalCode;
			}
		
			public void setBinType(String binType) {
				this.binType = binType;
			}
		
			public void setLatitude(Double latitude) {
				this.latitude = latitude;
			}
		
			public void setLongitude(Double longitude) {
				this.longitude = longitude;
			}
		
			public void setDistanceMeters(Double distanceMeters) {
				this.distanceMeters = distanceMeters;
			}
	
	

}
