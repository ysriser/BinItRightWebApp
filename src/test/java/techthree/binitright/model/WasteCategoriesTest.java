package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class WasteCategoriesTest {
    @Test
    void settersAndGetters_shouldWork() {
        WasteCategories wc = new WasteCategories();

        RecyclingInstructions ri = new RecyclingInstructions();
        List<Feedback> feedbacks = new ArrayList<>();
        List<CheckIn> checkIns = new ArrayList<>();

        wc.setCatId(1L);
        wc.setName("Plastic");
        wc.setStreamType(WasteCategories.StreamType.RECYCLABLE);
        wc.setIsHazardous(false);
        wc.setIconUrl("plastic.png");
        wc.setEmissionFactor(new BigDecimal("0.1234"));
        wc.setAvgWeight(new BigDecimal("0.2500"));
        wc.setRecyclingInstructions(ri);
        wc.setFeedback(feedbacks);
        wc.setCheckIns(checkIns);

        assertEquals(1L, wc.getCatId());
        assertEquals("Plastic", wc.getName());
        assertEquals(WasteCategories.StreamType.RECYCLABLE, wc.getStreamType());
        assertEquals(false, wc.getIsHazardous());
        assertEquals("plastic.png", wc.getIconUrl());
        assertEquals(new BigDecimal("0.1234"), wc.getEmissionFactor());
        assertEquals(new BigDecimal("0.2500"), wc.getAvgWeight());
        assertSame(ri, wc.getRecyclingInstructions());
        assertSame(feedbacks, wc.getFeedback());
        assertSame(checkIns, wc.getCheckIns());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        RecyclingInstructions ri = new RecyclingInstructions();
        List<Feedback> feedbacks = new ArrayList<>();
        List<CheckIn> checkIns = new ArrayList<>();

        WasteCategories wc = new WasteCategories(
                10L,
                "E-Waste",
                WasteCategories.StreamType.E_WASTE,
                true,
                "ewaste.png",
                new BigDecimal("1.5000"),
                new BigDecimal("0.8000"),
                ri,
                feedbacks,
                checkIns
        );

        assertEquals(10L, wc.getCatId());
        assertEquals("E-Waste", wc.getName());
        assertEquals(WasteCategories.StreamType.E_WASTE, wc.getStreamType());
        assertEquals(true, wc.getIsHazardous());
        assertEquals("ewaste.png", wc.getIconUrl());
        assertEquals(new BigDecimal("1.5000"), wc.getEmissionFactor());
        assertEquals(new BigDecimal("0.8000"), wc.getAvgWeight());
        assertSame(ri, wc.getRecyclingInstructions());
        assertSame(feedbacks, wc.getFeedback());
        assertSame(checkIns, wc.getCheckIns());
    }
}
