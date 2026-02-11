package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class NewsTest {
    @Test
    void settersAndGetters_shouldWork() {
        News news = new News();

        LocalDateTime published = LocalDateTime.of(2026, 2, 11, 9, 0);

        news.setNewsId(1L);
        news.setName("Recycling Campaign");
        news.setDescription("Join our community recycling campaign.");
        news.setImageUrl("news.png");
        news.setStatus(News.Status.UPCOMING);
        news.setPublishedDate(published);

        assertEquals(1L, news.getNewsId());
        assertEquals("Recycling Campaign", news.getName());
        assertEquals("Join our community recycling campaign.", news.getDescription());
        assertEquals("news.png", news.getImageUrl());
        assertEquals(News.Status.UPCOMING, news.getStatus());
        assertEquals(published, news.getPublishedDate());
    }

    @Test
    void constructor_withStatus_shouldSetFields() {
        News news = new News(
                10L,
                "Cleanup Drive",
                "Beach cleanup this weekend",
                "cleanup.jpg",
                News.Status.COMPLETED
        );

        assertEquals(10L, news.getNewsId());
        assertEquals("Cleanup Drive", news.getName());
        assertEquals("Beach cleanup this weekend", news.getDescription());
        assertEquals("cleanup.jpg", news.getImageUrl());
        assertEquals(News.Status.COMPLETED, news.getStatus());
        assertNull(news.getPublishedDate()); // not set in this constructor
    }

    @Test
    void constructor_withPublishedDate_shouldSetFields() {
        LocalDateTime published = LocalDateTime.of(2026, 1, 31, 18, 30);

        News news = new News(
                20L,
                "New Recycling Bins",
                "New bins installed across campus",
                "bins.jpg",
                published
        );

        assertEquals(20L, news.getNewsId());
        assertEquals("New Recycling Bins", news.getName());
        assertEquals("New bins installed across campus", news.getDescription());
        assertEquals("bins.jpg", news.getImageUrl());
        assertEquals(published, news.getPublishedDate());
        assertNull(news.getStatus()); // not set in this constructor
    }
}
