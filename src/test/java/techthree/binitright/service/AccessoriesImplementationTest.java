package techthree.binitright.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.model.Accessories;
import techthree.binitright.repository.AccessoriesRepository;

@ExtendWith(MockitoExtension.class)
public class AccessoriesImplementationTest {
    @Mock
    private AccessoriesRepository accessoriesRepository;

    @InjectMocks
    private AccessoriesImplementation accessoriesService;

    @Test
    void findAll_returnsAccessoriesFromRepository() {
        Accessories a1 = new Accessories();
        Accessories a2 = new Accessories();
        List<Accessories> expected = List.of(a1, a2);

        when(accessoriesRepository.findAll()).thenReturn(expected);

        List<Accessories> actual = accessoriesService.findAll();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(accessoriesRepository).findAll();
        verifyNoMoreInteractions(accessoriesRepository);
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_returnsEmptyList() {
        when(accessoriesRepository.findAll()).thenReturn(List.of());

        List<Accessories> actual = accessoriesService.findAll();

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        verify(accessoriesRepository).findAll();
        verifyNoMoreInteractions(accessoriesRepository);
    }

    @Test
    void findAll_whenRepositoryThrows_propagatesException() {
        when(accessoriesRepository.findAll()).thenThrow(new RuntimeException("DB down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> accessoriesService.findAll());
        assertEquals("DB down", ex.getMessage());

        verify(accessoriesRepository).findAll();
        verifyNoMoreInteractions(accessoriesRepository);
    }
}



