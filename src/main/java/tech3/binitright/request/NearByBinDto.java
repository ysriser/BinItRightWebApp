package tech3.binitright.request;

import java.math.BigDecimal;

import tech3.binitright.model.DropOffLocation.Status;

public class NearByBinDto {

        private String id;
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

		public NearByBinDto(final String dropOffId, final String name,
				final String address, final String description, final String postalCode,
					final String binType, final Status status, final BigDecimal latitude,
					final BigDecimal longitude, final double distance) {
	    	 this.id = dropOffId;
	         this.name = name;
	         this.address = address;
	         this.description = description;
	         this.postalCode = postalCode;
	         this.binType = binType;
	         this.status = status;
	         this.latitude = latitude.doubleValue();
	         this.longitude = longitude.doubleValue();
	         this.distanceMeters = distance;

			}

			public String getId() {
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

			public void setStatus(final Status status) {
				this.status = status;
			}

			public void setId(final String id) {
				this.id = id;
			}

			public void setName(final String name) {
				this.name = name;
			}

			public void setAddress(final String address) {
				this.address = address;
			}

			public void setDescription(final String description) {
				this.description = description;
			}

			public void setPostalCode(final String postalCode) {
				this.postalCode = postalCode;
			}

			public void setBinType(final String binType) {
				this.binType = binType;
			}

			public void setLatitude(final Double latitude) {
				this.latitude = latitude;
			}

			public void setLongitude(final Double longitude) {
				this.longitude = longitude;
			}

			public void setDistanceMeters(final Double distanceMeters) {
				this.distanceMeters = distanceMeters;
			}



}
