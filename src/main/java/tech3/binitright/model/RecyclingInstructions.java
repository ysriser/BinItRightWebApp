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
public final class RecyclingInstructions {

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

    public RecyclingInstructions(final Long recyclingInstructionsId, final WasteCategories wasteCategory, 
                                 final String locale, final String title, final String stepsUjson, 
                                 final String contaminationUrules) {
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

    public void setRecyclingInstructionsId(final Long recyclingInstructionsId) {
        this.recyclingInstructionsId = recyclingInstructionsId;
    }

    public WasteCategories getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategory(final WasteCategories wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getStepsUjson() {
        return stepsUjson;
    }

    public void setStepsUjson(final String stepsUjson) {
        this.stepsUjson = stepsUjson;
    }

    public String getContaminationUrules() {
        return contaminationUrules;
    }

    public void setContaminationUrules(final String contaminationUrules) {
        this.contaminationUrules = contaminationUrules;
    }
}