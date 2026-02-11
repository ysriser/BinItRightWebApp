package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import techthree.binitright.model.Accessories;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAccessories;
import techthree.binitright.repository.AccessoriesRepository;
import techthree.binitright.repository.UserAccessoriesRepository;
import techthree.binitright.repository.UserRepository;
import techthree.binitright.response.RedeemResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedeemServiceTest {
    @Mock private UserRepository userRepo;
    @Mock private AccessoriesRepository accessoryRepo;
    @Mock private UserAccessoriesRepository userAccessoryRepo;

    private RedeemService service;

    @BeforeEach
    void setUp() {
        service = new RedeemService(userRepo, accessoryRepo, userAccessoryRepo);
    }

    // ------- helpers -------
    private User user(Long id, Integer balance) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(balance);
        return u;
    }

    private Accessories accessory(Long id, int requiredPoints) {
        Accessories a = new Accessories();
        a.setAccessoriesId(id);
        a.setRequiredPoints(requiredPoints);
        return a;
    }

    // ------- tests -------

    @Test
    void getItems_returnsAllAccessories() {
        when(accessoryRepo.findAll()).thenReturn(List.of(new Accessories(), new Accessories()));

        List<Accessories> result = service.getItems();

        assertEquals(2, result.size());
        verify(accessoryRepo).findAll();
    }

    @Test
    void redeem_whenUserNotFound_throws() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.redeem(1L, 10L));

        assertTrue(ex.getMessage().contains("User not found"));
        verify(userRepo).findById(1L);
        verifyNoInteractions(accessoryRepo, userAccessoryRepo);
    }

    @Test
    void redeem_whenAccessoryNotFound_throws() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user(1L, 100)));
        when(accessoryRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.redeem(1L, 10L));

        assertTrue(ex.getMessage().contains("Accessory not found"));
        verify(userRepo).findById(1L);
        verify(accessoryRepo).findById(10L);
        verifyNoInteractions(userAccessoryRepo);
    }

    @Test
    void redeem_whenAlreadyOwned_returnsAlreadyOwned_noSave() {
        User u = user(1L, 100);
        Accessories a = accessory(10L, 50);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(accessoryRepo.findById(10L)).thenReturn(Optional.of(a));
        when(userAccessoryRepo.existsByUser_IdAndAccessories_AccessoriesId(1L, 10L))
                .thenReturn(true);

        RedeemResponse res = service.redeem(1L, 10L);

        assertNotNull(res);
        assertEquals(100, res.getNewTotalPoints());              // adjust getter name if needed
        assertEquals("Already owned", res.getMessage());  // adjust getter name if needed

        verify(userRepo, never()).save(any());
        verify(userAccessoryRepo, never()).save(any());
    }

    @Test
    void redeem_whenNotEnoughPoints_returnsNotEnoughPoints_noSave() {
        User u = user(1L, 30);
        Accessories a = accessory(10L, 50);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(accessoryRepo.findById(10L)).thenReturn(Optional.of(a));
        when(userAccessoryRepo.existsByUser_IdAndAccessories_AccessoriesId(1L, 10L))
                .thenReturn(false);

        RedeemResponse res = service.redeem(1L, 10L);

        assertNotNull(res);
        assertEquals(30, res.getNewTotalPoints());                   // adjust getter name if needed
        assertEquals("Not enough points", res.getMessage());  // adjust getter name if needed

        verify(userRepo, never()).save(any());
        verify(userAccessoryRepo, never()).save(any());
    }

    @Test
    void redeem_whenSuccess_deductsPoints_savesUser_andSavesOwnership() {
        User u = user(1L, 100);
        Accessories a = accessory(10L, 40);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(accessoryRepo.findById(10L)).thenReturn(Optional.of(a));
        when(userAccessoryRepo.existsByUser_IdAndAccessories_AccessoriesId(1L, 10L))
                .thenReturn(false);

        // echo back saved objects
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userAccessoryRepo.save(any(UserAccessories.class))).thenAnswer(inv -> inv.getArgument(0));

        RedeemResponse res = service.redeem(1L, 10L);

        assertNotNull(res);
        assertEquals(60, res.getNewTotalPoints());                       // 100 - 40
        assertEquals("Redeemed successfully", res.getMessage());

        // verify user saved with new balance
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        assertEquals(60, userCaptor.getValue().getPointBalance());

        // verify ownership saved
        ArgumentCaptor<UserAccessories> uaCaptor = ArgumentCaptor.forClass(UserAccessories.class);
        verify(userAccessoryRepo).save(uaCaptor.capture());

        UserAccessories savedUa = uaCaptor.getValue();
        assertEquals(u, savedUa.getUser());
        assertEquals(a, savedUa.getAccessories());
        assertFalse(savedUa.isEquipped()); // if getter is getEquipped(), change accordingly
    }

    @Test
    void redeem_whenUserBalanceNull_treatedAsZero_notEnoughPoints() {
        User u = user(1L, null);
        Accessories a = accessory(10L, 10);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(accessoryRepo.findById(10L)).thenReturn(Optional.of(a));
        when(userAccessoryRepo.existsByUser_IdAndAccessories_AccessoriesId(1L, 10L))
                .thenReturn(false);

        RedeemResponse res = service.redeem(1L, 10L);

        assertEquals(0, res.getNewTotalPoints());
        assertEquals("Not enough points", res.getMessage());

        verify(userRepo, never()).save(any());
        verify(userAccessoryRepo, never()).save(any());
    }
}

