package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.Achievement;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.model.UserAchievement;
import tech3.binitright.repository.AccessoriesRepository;
import tech3.binitright.repository.AchievementRepository;
import tech3.binitright.repository.UserAccessoriesRepository;
import tech3.binitright.repository.UserAchievementRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.response.RedeemResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardShopServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessoriesRepository accessoryRepository;
    @Mock
    private UserAccessoriesRepository userAccessoriesRepository;

    // Repos needed to build a REAL AchievementImplementation
    @Mock
    private AchievementRepository achievementRepo;
    @Mock
    private UserAchievementRepository userAchievementRepo;
    @Mock
    private UserRepository achievementUserRepo; // same type, used by AchievementImplementation

    private AchievementImplementation achievementImplementation; // REAL instance
    private RewardShopService service;

    @BeforeEach
    void setUp() {
        // ✅ REAL AchievementImplementation (no Mockito mock => no ByteBuddy problem)
        achievementImplementation =
                new AchievementImplementation(achievementRepo, userAchievementRepo, achievementUserRepo);

        service = new RewardShopService(
                userRepository,
                accessoryRepository,
                userAccessoriesRepository,
                achievementImplementation
        );
    }

    // ---------- helpers ----------
    private User user(Long id, Integer balance, int rank) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(balance);
        u.setCurrentRank(rank);
        return u;
    }

    private Accessories accessory(Long id, String name, int points) {
        Accessories a = new Accessories();
        a.setAccessoriesId(id);
        a.setName(name);
        a.setRequiredPoints(points);
        return a;
    }

    private UserAccessories owned(User u, Accessories a, boolean equipped) {
        UserAccessories ua = new UserAccessories();
        ua.setUser(u);
        ua.setAccessories(a);
        ua.setEquipped(equipped);
        return ua;
    }

    private Achievement achievement(Long id) {
        Achievement ach = new Achievement();
        ach.setAchievementId(id);
        ach.setName("dummy");
        return ach;
    }



    @Test
    void getItems_returnsAllAccessories() {
        when(accessoryRepository.findAll()).thenReturn(List.of(
                accessory(1L, "Hat", 100),
                accessory(2L, "Shirt", 200)
        ));

        List<Accessories> items = service.getItems();

        assertEquals(2, items.size());
        verify(accessoryRepository).findAll();
    }

    @Test
    void getItemsForUser_marksOwnedAndEquippedCorrectly() {
        Long userId = 7L;

        Accessories a1 = accessory(1L, "Hat", 100);
        Accessories a2 = accessory(2L, "Shirt", 200);
        Accessories a3 = accessory(3L, "Shoes", 300);

        when(accessoryRepository.findAll()).thenReturn(List.of(a1, a2, a3));

        User u = user(userId, 500, 1);
        // user owns a2 (equipped), owns a3 (not equipped)
        when(userAccessoriesRepository.findAllByUser_Id(userId)).thenReturn(List.of(
                owned(u, a2, true),
                owned(u, a3, false)
        ));

        List<ShopItemDTO> result = service.getItemsForUser(userId);

        assertEquals(3, result.size());

        ShopItemDTO i1 = result.get(0);
        ShopItemDTO i2 = result.get(1);
        ShopItemDTO i3 = result.get(2);

        assertEquals(1L, i1.getAccessoriesId());
        assertFalse(i1.isOwned());
        assertFalse(i1.isEquipped());

        assertEquals(2L, i2.getAccessoriesId());
        assertTrue(i2.isOwned());
        assertTrue(i2.isEquipped());

        assertEquals(3L, i3.getAccessoriesId());
        assertTrue(i3.isOwned());
        assertFalse(i3.isEquipped());
    }

    @Test
    void redeem_whenAlreadyOwned_returnsAlreadyOwned_andDoesNotSave() {
        Long userId = 1L;
        Long accessoriesId = 10L;

        User u = user(userId, 500, 1);
        Accessories a = accessory(accessoriesId, "Hat", 200);

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(accessoryRepository.findById(accessoriesId)).thenReturn(Optional.of(a));
        when(userAccessoriesRepository.existsByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(true);

        RedeemResponse res = service.redeem(userId, accessoriesId);

        // adjust if RedeemResponse is record: res.balance(), res.message()
        assertEquals(500, res.getNewTotalPoints());
        assertEquals("Already owned", res.getMessage());

        verify(userRepository, never()).save(any());
        verify(userAccessoriesRepository, never()).save(any());
    }

    @Test
    void redeem_whenNotEnoughPoints_returnsNotEnoughPoints_andDoesNotSave() {
        Long userId = 1L;
        Long accessoriesId = 10L;

        User u = user(userId, 50, 1);
        Accessories a = accessory(accessoriesId, "Hat", 200);

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(accessoryRepository.findById(accessoriesId)).thenReturn(Optional.of(a));
        when(userAccessoriesRepository.existsByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(false);

        RedeemResponse res = service.redeem(userId, accessoriesId);

        assertEquals(50, res.getNewTotalPoints());
        assertEquals("Not enough points", res.getMessage());

        verify(userRepository, never()).save(any());
        verify(userAccessoriesRepository, never()).save(any());
    }

    @Test
    void redeem_whenSuccess_deductsPoints_savesOwnership_andUnlocksAchievement10() {
        Long userId = 1L;
        Long accessoriesId = 10L;

        User u = user(userId, 500, 1);
        Accessories a = accessory(accessoriesId, "Hat", 200);

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(accessoryRepository.findById(accessoriesId)).thenReturn(Optional.of(a));
        when(userAccessoriesRepository.existsByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId))
                .thenReturn(false);

        // user save echo
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userAccessoriesRepository.save(any(UserAccessories.class))).thenAnswer(inv -> inv.getArgument(0));

        // --- prepare AchievementImplementation dependencies so unlockAchievement() works ---
        when(userAchievementRepo.findByUser_Id(userId)).thenReturn(List.of()); // not unlocked yet
        when(achievementUserRepo.findById(userId)).thenReturn(Optional.of(u)); // AchievementImplementation.userRepo
        when(achievementRepo.findById(10L)).thenReturn(Optional.of(achievement(10L)));
        when(userAchievementRepo.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        RedeemResponse res = service.redeem(userId, accessoriesId);

        assertEquals(300, res.getNewTotalPoints()); // 500 - 200
        assertEquals("Redeemed successfully", res.getMessage());

        // verify user balance saved
        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCap.capture());
        assertEquals(300, userCap.getValue().getPointBalance());

        // verify ownership row saved
        ArgumentCaptor<UserAccessories> uaCap = ArgumentCaptor.forClass(UserAccessories.class);
        verify(userAccessoriesRepository).save(uaCap.capture());
        assertEquals(u, uaCap.getValue().getUser());
        assertEquals(a, uaCap.getValue().getAccessories());
        assertFalse(uaCap.getValue().isEquipped()); // if getter is getEquipped(), change this line

        // verify achievement unlocked via repo save
        verify(userAchievementRepo).save(any(UserAchievement.class));
        verify(achievementRepo).findById(10L);
    }

    @Test
    void redeem_whenUserNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.redeem(99L, 1L));

        assertTrue(ex.getMessage().contains("User not found"));
        verifyNoInteractions(accessoryRepository, userAccessoriesRepository);
    }

    @Test
    void redeem_whenAccessoryNotFound_throws() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user(userId, 10, 1)));
        when(accessoryRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.redeem(userId, 123L));

        assertTrue(ex.getMessage().contains("Accessory not found"));
        verify(userRepository).findById(userId);
        verify(accessoryRepository).findById(123L);
        verifyNoMoreInteractions(userAccessoriesRepository);
    }
}
