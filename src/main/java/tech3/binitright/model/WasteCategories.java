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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "waste_categories")
public class WasteCategories {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "cat_id")
	    private Long catId;

	    private String name;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "stream_type")
	    private StreamType streamType;

	    @Column(name = "is_hazardous")
	    private Boolean isHazardous;

	    @Column(name = "icon_url")
	    private String iconUrl;

	    @Column(name = "emission_factor", precision = 10, scale = 4)
	    private BigDecimal emissionFactor;

	    @Column(name = "avg_weight")
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
            E_WASTE,
            HAZARDOUS
	    }
	    
	    public WasteCategories() {}

		public WasteCategories(Long catId, String name, StreamType streamType, Boolean isHazardous, String iconUrl,
				BigDecimal emissionFactor, BigDecimal avgWeight, RecyclingInstructions recyclingInstructions,
				List<Feedback> feedback, List<CheckIn> checkIns) {
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

		public void setCatId(Long catId) {
			this.catId = catId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public StreamType getStreamType() {
			return streamType;
		}

		public void setStreamType(StreamType streamType) {
			this.streamType = streamType;
		}

		public Boolean getIsHazardous() {
			return isHazardous;
		}

		public void setIsHazardous(Boolean isHazardous) {
			this.isHazardous = isHazardous;
		}

		public String getIconUrl() {
			return iconUrl;
		}

		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}

		public BigDecimal getEmissionFactor() {
			return emissionFactor;
		}

		public void setEmissionFactor(BigDecimal emissionFactor) {
			this.emissionFactor = emissionFactor;
		}

		public BigDecimal getAvgWeight() {
			return avgWeight;
		}

		public void setAvgWeight(BigDecimal avgWeight) {
			this.avgWeight = avgWeight;
		}

		public RecyclingInstructions getRecyclingInstructions() {
			return recyclingInstructions;
		}

		public void setRecyclingInstructions(RecyclingInstructions recyclingInstructions) {
			this.recyclingInstructions = recyclingInstructions;
		}

		public List<Feedback> getFeedback() {
			return feedback;
		}

		public void setFeedback(List<Feedback> feedback) {
			this.feedback = feedback;
		}

		public List<CheckIn> getCheckIns() {
			return checkIns;
		}

		public void setCheckIns(List<CheckIn> checkIns) {
			this.checkIns = checkIns;
		}
	    
	    

}