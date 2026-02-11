package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class FeedbackTest {
    @Test
    void settersAndGetters_shouldWork() {
        Feedback feedback = new Feedback();

        User user = new User();
        WasteCategories waste = new WasteCategories();

        feedback.setFeedbackId(1L);
        feedback.setUser(user);
        feedback.setWasteCategories(waste);
        feedback.setImageUrl("image.png");
        feedback.setFeedbackContent("Good recycling system");

        assertEquals(1L, feedback.getFeedbackId());
        assertSame(user, feedback.getUser());
        assertSame(waste, feedback.getWasteCategories());
        assertEquals("image.png", feedback.getImageUrl());
        assertEquals("Good recycling system", feedback.getFeedbackContent());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        User user = new User();
        WasteCategories waste = new WasteCategories();

        Feedback feedback = new Feedback(
                5L,
                user,
                waste,
                "test.jpg",
                "Needs improvement"
        );

        assertEquals(5L, feedback.getFeedbackId());
        assertSame(user, feedback.getUser());
        assertSame(waste, feedback.getWasteCategories());
        assertEquals("test.jpg", feedback.getImageUrl());
        assertEquals("Needs improvement", feedback.getFeedbackContent());
    }
}
