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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
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

}