package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.UserAccessoriesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAccessoriesImplementationTest {
    @Mock
    private UserAccessoriesRepository userAccessoriesRepository;

    private UserAccessoriesImplementation service;

    @BeforeEach
    void setUp() {
        service = new UserAccessoriesImplementation();

        // field injection (@Autowired) -> set manually (no Spring context)
        try {
            var f = UserAccessoriesImplementation.class.getDeclaredField("userAccessoriesRepository");
            f.setAccessible(true);
            f.set(service, userAccessoriesRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // -------- helpers --------
    private User user(Long id) {
        User u = new User();
        u.setId(id);
        return u;
    }

    private Accessories accessory(Long id) {
        Accessories a = new Accessories();
        a.setAccessoriesId(id);
        return a;
    }

    private UserAccessories ua(Long userId, Long accessoriesId, boolean equipped) {
        UserAccessories ua = new UserAccessories();
        ua.setUser(user(userId));
        ua.setAccessories(accessory(accessoriesId));
        ua.setEquipped(equipped);
        return ua;
    }

    @Test
    void save_callsRepositorySave() {
        UserAccessories item = ua(1L, 10L, false);

        service.save(item);

        verify(userAccessoriesRepository).save(item);
    }

    @Test
    void findAllByUser_Id_returnsRepositoryResult() {
        Long userId = 1L;
        List<UserAccessories> repoList = List.of(
                ua(userId, 10L, false),
                ua(userId, 11L, true)
        );

        when(userAccessoriesRepository.findAllByUser_Id(userId)).thenReturn(repoList);

        List<UserAccessories> result = service.findAllByUser_Id(userId);

        assertSame(repoList, result);
        verify(userAccessoriesRepository).findAllByUser_Id(userId);
    }

    @Test
    void equipItem_unequipsCurrentItems_thenEquipsSelectedItem() {
        Long userId = 1L;
        Long toEquipId = 10L;

        // current equipped
        UserAccessories equipped1 = ua(userId, 20L, true);
        UserAccessories equipped2 = ua(userId, 21L, true);

        List<UserAccessories> currentlyEquipped = new ArrayList<>(List.of(equipped1, equipped2));

        when(userAccessoriesRepository.findByUser_IdAndEquippedTrue(userId))
                .thenReturn(currentlyEquipped);

        // item to equip (owned)
        UserAccessories itemToEquip = ua(userId, toEquipId, false);
        when(userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, toEquipId))
                .thenReturn(itemToEquip);

        service.equipItem(userId, toEquipId);

        assertFalse(equipped1.isEquipped());
        assertFalse(equipped2.isEquipped());

        ArgumentCaptor<List<UserAccessories>> saveAllCaptor = ArgumentCaptor.forClass(List.class);
        verify(userAccessoriesRepository).saveAll(saveAllCaptor.capture());
        assertEquals(2, saveAllCaptor.getValue().size());

        assertTrue(itemToEquip.isEquipped());
        verify(userAccessoriesRepository).save(itemToEquip);

        verify(userAccessoriesRepository).findByUser_IdAndEquippedTrue(userId);
        verify(userAccessoriesRepository).findByUser_IdAndAccessories_AccessoriesId(userId, toEquipId);
    }

    @Test
    void equipItem_whenAccessoryNotOwned_throws_andDoesNotSaveSelected() {
        Long userId = 1L;
        Long accessoriesId = 99L;

        List<UserAccessories> currentlyEquipped = new ArrayList<>(List.of(
                ua(userId, 20L, true)
        ));

        when(userAccessoriesRepository.findByUser_IdAndEquippedTrue(userId))
                .thenReturn(currentlyEquipped);

        when(userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.equipItem(userId, accessoriesId));

        assertTrue(ex.getMessage().contains("Accessory not owned"));

        verify(userAccessoriesRepository).saveAll(anyList());

        verify(userAccessoriesRepository, never()).save(isNull());
    }

    @Test
    void unequipItem_whenOwned_setsEquippedFalse_andSaves() {
        Long userId = 1L;
        Long accessoriesId = 10L;

        UserAccessories item = ua(userId, accessoriesId, true);

        when(userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(item);

        service.unequipItem(userId, accessoriesId);

        assertFalse(item.isEquipped());
        verify(userAccessoriesRepository).save(item);
    }

    @Test
    void unequipItem_whenNotOwned_doesNothing() {
        Long userId = 1L;
        Long accessoriesId = 10L;

        when(userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(null);

        service.unequipItem(userId, accessoriesId);

        verify(userAccessoriesRepository, never()).save(any());
    }
}

