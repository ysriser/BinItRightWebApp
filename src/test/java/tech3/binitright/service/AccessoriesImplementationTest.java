package tech3.binitright.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech3.binitright.model.Accessories;
import tech3.binitright.repository.AccessoriesRepository;

@ExtendWith(MockitoExtension.class)
public class AccessoriesImplementationTest {
    @Mock
    private AccessoriesRepository accessoriesRepository;

    @InjectMocks
    private AccessoriesImplementation accessoriesService;

    @Test
    void findAll_returnsAccessoriesFromRepository() {
        // Arrange
        Accessories a1 = new Accessories();
        Accessories a2 = new Accessories();
        List<Accessories> expected = List.of(a1, a2);

        when(accessoriesRepository.findAll()).thenReturn(expected);

        // Act
        List<Accessories> actual = accessoriesService.findAll();

        // Assert
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertSame(expected, actual); // it returns the same list instance from repo
        verify(accessoriesRepository, times(1)).findAll();
        verifyNoMoreInteractions(accessoriesRepository);
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_returnsEmptyList() {
        // Arrange
        when(accessoriesRepository.findAll()).thenReturn(List.of());

        // Act
        List<Accessories> actual = accessoriesService.findAll();

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        verify(accessoriesRepository, times(1)).findAll();
        verifyNoMoreInteractions(accessoriesRepository);
    }
}



