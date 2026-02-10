package tech3.binitright.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.interfacemethods.NewsInterface;
import tech3.binitright.model.News;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class NewsRestControllerTest {

    @Test
    void getAllNewsReturnsServiceData() {
        final NewsRestController controller = new NewsRestController();
        final NewsInterface newsService = Mockito.mock(NewsInterface.class);
        ReflectionTestUtils.setField(controller, "newsService", newsService);

        final News news1 = new News(1L, "Title A", "Desc A", "img-a", LocalDateTime.now());
        final News news2 = new News(2L, "Title B", "Desc B", "img-b", LocalDateTime.now().minusDays(1));
        when(newsService.findAllByOrderByPublishedDateDesc()).thenReturn(List.of(news1, news2));

        final ResponseEntity<List<News>> response = controller.getAllNews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Title A", response.getBody().get(0).getName());
    }

    @Test
    void getNewsByIdReturnsSingleNews() {
        final NewsRestController controller = new NewsRestController();
        final NewsInterface newsService = Mockito.mock(NewsInterface.class);
        ReflectionTestUtils.setField(controller, "newsService", newsService);

        final News news = new News(9L, "Headline", "Details", "img", LocalDateTime.now());
        when(newsService.getNewsByNewsId(9L)).thenReturn(news);

        final ResponseEntity<News> response = controller.getNewsById(9L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Headline", response.getBody().getName());
        assertEquals(9L, response.getBody().getNewsId());
    }
}
