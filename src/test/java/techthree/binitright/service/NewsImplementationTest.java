package techthree.binitright.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.model.News;
import techthree.binitright.repository.NewsRepository;

@ExtendWith(MockitoExtension.class)
public class NewsImplementationTest {
    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsImplementation newsImplementation;

    // ---------- helper ----------
    private News news(Long id) {
        News n = new News();
        n.setNewsId(id);
        n.setName("Test News");
        n.setDescription("Some content");
        n.setPublishedDate(LocalDateTime.now());
        return n;
    }

    // ---------- tests ----------

    @Test
    void findAllByOrderByPublishedDateDesc_returnsNewsList() {
        List<News> mockNews = List.of(
                news(1L),
                news(2L)
        );

        when(newsRepository.findAllByOrderByPublishedDateDesc())
                .thenReturn(mockNews);

        List<News> result =
                newsImplementation.findAllByOrderByPublishedDateDesc();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getNewsId());

        verify(newsRepository).findAllByOrderByPublishedDateDesc();
    }

    @Test
    void findAllByOrderByPublishedDateDesc_whenEmpty_returnsEmptyList() {
        when(newsRepository.findAllByOrderByPublishedDateDesc())
                .thenReturn(List.of());

        List<News> result =
                newsImplementation.findAllByOrderByPublishedDateDesc();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(newsRepository).findAllByOrderByPublishedDateDesc();
    }

    @Test
    void getNewsByNewsId_whenExists_returnsNews() {
        News mockNews = news(10L);

        when(newsRepository.findNewsByNewsId(10L))
                .thenReturn(mockNews);

        News result =
                newsImplementation.getNewsByNewsId(10L);

        assertNotNull(result);
        assertEquals(10L, result.getNewsId());
        assertEquals("Test News", result.getName());

        verify(newsRepository).findNewsByNewsId(10L);
    }

    @Test
    void getNewsByNewsId_whenNotFound_returnsNull() {
        when(newsRepository.findNewsByNewsId(99L))
                .thenReturn(null);

        News result =
                newsImplementation.getNewsByNewsId(99L);

        assertNull(result);

        verify(newsRepository).findNewsByNewsId(99L);
    }
}

