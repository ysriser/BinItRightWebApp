package tech3.binitright.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("recyclingUinstructions")
public class RecyclingInstructions{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recyclingUinstructionsUid")
    private Long recyclingInstructionsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catUid", nullable = false)
    private WasteCategories wasteCategory;

    private String locale;

    private String title;

    private String stepsUjson;

    private String contaminationUrules;
    
    public RecyclingInstructions() {}

    public RecyclingInstructions(Long recyclingInstructionsId, WasteCategories wasteCategory, String locale,
			String title, String stepsUjson, String contaminationUrules) {
		super();
		this.recyclingInstructionsId = recyclingInstructionsId;
		this.wasteCategory = wasteCategory;
		this.locale = locale;
		this.title = title;
		this.stepsUjson = stepsUjson;
		this.contaminationUrules = contaminationUrules;
	}

	public Long getRecyclingInstructionsId() {
        return recyclingInstructionsId;
    }

    public void setRecyclingInstructionsId(Long recyclingInstructionsId) {
        this.recyclingInstructionsId = recyclingInstructionsId;
    }

    public WasteCategories getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategories(WasteCategories wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStepsUjson() {
        return stepsUjson;
    }

    public void setStepsUjson(String stepsUjson) {
        this.stepsUjson = stepsUjson;
    }

    public String getContaminationUrules() {
        return contaminationUrules;
    }

    public void setContaminationUrules(String ContaminationUrules) {
        this.contaminationUrules = ContaminationUrules;
    }


}
