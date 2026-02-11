package techthree.binitright.model;

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
@DiscriminatorValue("recycling_instructions")
public class RecyclingInstructions{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recycling_instructions_id")
    private Long recyclingInstructionsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", nullable = false)
    private WasteCategories wasteCategory;

    private String locale;

    private String title;

    @Column(name = "steps_json")
    private String stepsJson;

    @Column(name = "contamination_rules")
    private String contaminationRules;
    
    public RecyclingInstructions() {}

    public RecyclingInstructions(Long recyclingInstructionsId, WasteCategories wasteCategory, String locale,
			String title, String stepsJson, String contaminationRules) {
		super();
		this.recyclingInstructionsId = recyclingInstructionsId;
		this.wasteCategory = wasteCategory;
		this.locale = locale;
		this.title = title;
		this.stepsJson = stepsJson;
		this.contaminationRules = contaminationRules;
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

    public String getSteps_json() {
        return stepsJson;
    }

    public void setSteps_json(String stepsJson) {
        this.stepsJson = stepsJson;
    }

    public String getContamination_rules() {
        return contaminationRules;
    }

    public void setContamination_rules(String contaminationRules) {
        this.contaminationRules = contaminationRules;
    }


}
