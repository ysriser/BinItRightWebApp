package tech3.binitright.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wasteUcategories")
public class WasteCategories {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "catUid")
	    private Long catId;

	    private String name;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "streamUtype")
	    private StreamType streamType;

	    @Column(name = "isUhazardous")
	    private Boolean isHazardous;

	    @Column(name = "iconUurl")
	    private String iconUrl;

	    @Column(name = "emissionUfactor", precision = 10, scale = 4)
	    private BigDecimal emissionFactor;

	    @Column(name = "avgUweight")
	    private BigDecimal avgWeight;

	    @OneToOne(mappedBy = "wasteCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private RecyclingInstructions recyclingInstructions;

	    @OneToMany(mappedBy = "wasteCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<Feedback> feedback;

	    @OneToMany(mappedBy = "wasteCategories", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<CheckIn> checkIns;

	    public enum StreamType {
            RECYCLABLE,
            GENERAL,
            ORGANIC,
            EUWASTE,
            HAZARDOUS
	    }

	    public WasteCategories() {}

		public WasteCategories(final Long catId, final String name, final StreamType streamType,
				final Boolean isHazardous, final String iconUrl,
				final BigDecimal emissionFactor, final BigDecimal avgWeight,
				final RecyclingInstructions recyclingInstructions,
				final List<Feedback> feedback, final List<CheckIn> checkIns) {
			super();
			this.catId = catId;
			this.name = name;
			this.streamType = streamType;
			this.isHazardous = isHazardous;
			this.iconUrl = iconUrl;
			this.emissionFactor = emissionFactor;
			this.avgWeight = avgWeight;
			this.recyclingInstructions = recyclingInstructions;
			this.feedback = feedback;
			this.checkIns = checkIns;
		}

		public Long getCatId() {
			return catId;
		}

		public void setCatId(final Long catId) {
			this.catId = catId;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public StreamType getStreamType() {
			return streamType;
		}

		public void setStreamType(final StreamType streamType) {
			this.streamType = streamType;
		}

		public Boolean getIsHazardous() {
			return isHazardous;
		}

		public void setIsHazardous(final Boolean isHazardous) {
			this.isHazardous = isHazardous;
		}

		public String getIconUrl() {
			return iconUrl;
		}

		public void setIconUrl(final String iconUrl) {
			this.iconUrl = iconUrl;
		}

		public BigDecimal getEmissionFactor() {
			return emissionFactor;
		}

		public void setEmissionFactor(final BigDecimal emissionFactor) {
			this.emissionFactor = emissionFactor;
		}

		public BigDecimal getAvgWeight() {
			return avgWeight;
		}

		public void setAvgWeight(final BigDecimal avgWeight) {
			this.avgWeight = avgWeight;
		}

		public RecyclingInstructions getRecyclingInstructions() {
			return recyclingInstructions;
		}

		public void setRecyclingInstructions(final RecyclingInstructions recyclingInstructions) {
			this.recyclingInstructions = recyclingInstructions;
		}

		public List<Feedback> getFeedback() {
			return feedback;
		}

		public void setFeedback(final List<Feedback> feedback) {
			this.feedback = feedback;
		}

		public List<CheckIn> getCheckIns() {
			return checkIns;
		}

		public void setCheckIns(final List<CheckIn> checkIns) {
			this.checkIns = checkIns;
		}



}