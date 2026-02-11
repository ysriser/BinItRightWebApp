package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecyclingInstructionsTest {
    @Test
    void settersAndGetters_shouldWork() {
        RecyclingInstructions ri = new RecyclingInstructions();

        WasteCategories waste = new WasteCategories();

        ri.setRecyclingInstructionsId(1L);
        ri.setWasteCategories(waste);
        ri.setLocale("en_SG");
        ri.setTitle("How to recycle plastic");
        ri.setSteps_json("[\"Rinse\",\"Dry\",\"Put in blue bin\"]");
        ri.setContamination_rules("No food residue");

        assertEquals(1L, ri.getRecyclingInstructionsId());
        assertSame(waste, ri.getWasteCategory());
        assertEquals("en_SG", ri.getLocale());
        assertEquals("How to recycle plastic", ri.getTitle());
        assertEquals("[\"Rinse\",\"Dry\",\"Put in blue bin\"]", ri.getSteps_json());
        assertEquals("No food residue", ri.getContamination_rules());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        WasteCategories waste = new WasteCategories();

        RecyclingInstructions ri = new RecyclingInstructions(
                10L,
                waste,
                "ta_IN",
                "Plastic Recycling",
                "[\"Step1\",\"Step2\"]",
                "Keep it clean"
        );

        assertEquals(10L, ri.getRecyclingInstructionsId());
        assertSame(waste, ri.getWasteCategory());
        assertEquals("ta_IN", ri.getLocale());
        assertEquals("Plastic Recycling", ri.getTitle());
        assertEquals("[\"Step1\",\"Step2\"]", ri.getSteps_json());
        assertEquals("Keep it clean", ri.getContamination_rules());
    }
}
