package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.response.RecycleHistoryResponse;
import techthree.binitright.repository.CheckInRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecycleHistoryServiceTest {
    @Mock
    private CheckInRepository repository;

    private RecycleHistoryService service;

    @BeforeEach
    void setUp() {
        service = new RecycleHistoryService(repository);
    }

    @Test
    void getRecycleHistory_whenRepoReturnsNonEmpty_returnsSameList() {
        Long userId = 10L;

        // ✅ Works even if RecycleHistoryResponse needs 4 args, because we don't instantiate it.
        List<RecycleHistoryResponse> repoResult =
                Arrays.asList((RecycleHistoryResponse) null, null);

        when(repository.findRecycleHistoryByUserId(userId))
                .thenReturn(repoResult);

        List<RecycleHistoryResponse> result = service.getRecycleHistory(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(repoResult, result); // service returns the same list instance

        verify(repository).findRecycleHistoryByUserId(userId);
    }

    @Test
    void getRecycleHistory_whenRepoReturnsEmpty_returnsEmptyList() {
        Long userId = 10L;

        when(repository.findRecycleHistoryByUserId(userId))
                .thenReturn(List.of());

        List<RecycleHistoryResponse> result = service.getRecycleHistory(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findRecycleHistoryByUserId(userId);
    }

    @Test
    void getRecycleHistory_whenRepoReturnsNull_returnsEmptyList() {
        Long userId = 10L;

        when(repository.findRecycleHistoryByUserId(userId))
                .thenReturn(null);

        List<RecycleHistoryResponse> result = service.getRecycleHistory(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findRecycleHistoryByUserId(userId);
    }
}