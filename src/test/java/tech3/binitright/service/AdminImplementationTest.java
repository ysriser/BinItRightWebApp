package tech3.binitright.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import tech3.binitright.model.Admin;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminImplementationTest {
    @Mock private AdminRepository adminRepository;
    @Mock private AdminRepository adminrepo; // because your service has BOTH
    @Mock private UserRepository userRepository;
    @Mock private CheckInRepository checkInRepository;
    @InjectMocks private AdminImplementation service;

    private Admin admin(long id, String username) {
        Admin a = new Admin();
        a.setId(id);
        a.setUsername(username);
        return a;
    }

    private User user(long id, Integer pointBalance) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(pointBalance);
        return u;
    }

    private CheckIn checkIn(long id, int quantity, User user) {
        CheckIn c = new CheckIn();
        c.setCheckInId(id); // if your field is "id", change to setId(id)
        c.setQuantity(quantity);
        c.setUser(user);
        c.setStatus(CheckIn.Status.PROCESSING);
        return c;
    }


    @Test
    void saveAdmin_savesViaAdminrepo() {
        Admin a = admin(1L, "admin1");

        service.saveAdmin(a);

        verify(adminrepo).save(a);
        verifyNoMoreInteractions(adminrepo);
    }


    @Test
    void findAdminByUsername_returnsListFromRepo() {
        when(adminrepo.findByUsername("alice"))
                .thenReturn(List.of(admin(1, "alice"), admin(2, "alice")));

        List<Admin> result = service.findAdminByUsername("alice");

        assertEquals(2, result.size());
        verify(adminrepo).findByUsername("alice");
    }


    @Test
    void getSingleAdminByUsername_returnsFirstAdmin() {
        Admin a = admin(1, "bob");
        when(adminrepo.findByUsername("bob")).thenReturn(List.of(a));

        Admin result = service.getSingleAdminByUsername("bob");

        assertEquals("bob", result.getUsername());
        verify(adminrepo).findByUsername("bob");
    }

    @Test
    void getSingleAdminByUsername_whenNotFound_throws() {
        when(adminrepo.findByUsername("missing")).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getSingleAdminByUsername("missing"));

        assertTrue(ex.getMessage().contains("Admin not found"));
        verify(adminrepo).findByUsername("missing");
    }


    @Test
    void getPendingCheckIns_callsRepoWithProcessing() {
        List<CheckIn> pending = List.of();
        when(checkInRepository.findByStatusWithDetails(CheckIn.Status.PROCESSING))
                .thenReturn(pending);

        List<CheckIn> result = service.getPendingCheckIns();

        assertSame(pending, result);
        verify(checkInRepository).findByStatusWithDetails(CheckIn.Status.PROCESSING);
    }


    @Test
    void updateCheckInStatus_whenApproved_setsRewardsUpdatesUserAndSaves() {
        long checkInId = 10L;
        User u = user(99L, 100); // existing balance 100
        CheckIn c = checkIn(checkInId, 3, u); // quantity 3 => rewards 30

        when(checkInRepository.findById(checkInId)).thenReturn(Optional.of(c));

        service.updateCheckInStatus(checkInId, CheckIn.Status.APPROVED, "ok");

        assertEquals(CheckIn.Status.APPROVED, c.getStatus());
        assertEquals(30, c.getRewardPoints());
        assertEquals(130, u.getPointBalance());

        verify(userRepository).save(u);
        verify(checkInRepository).save(c);
    }

    @Test
    void updateCheckInStatus_whenApproved_userBalanceNull_treatsAsZero() {
        long checkInId = 11L;
        User u = user(99L, null); // null balance
        CheckIn c = checkIn(checkInId, 2, u); // rewards 20

        when(checkInRepository.findById(checkInId)).thenReturn(Optional.of(c));

        service.updateCheckInStatus(checkInId, CheckIn.Status.APPROVED, "ok");

        assertEquals(CheckIn.Status.APPROVED, c.getStatus());
        assertEquals(20, c.getRewardPoints());
        assertEquals(20, u.getPointBalance());

        verify(userRepository).save(u);
        verify(checkInRepository).save(c);
    }

    @Test
    void updateCheckInStatus_whenRejected_setsRewardZero_savesOnlyCheckIn() {
        long checkInId = 12L;
        User u = user(99L, 100);
        CheckIn c = checkIn(checkInId, 5, u);

        when(checkInRepository.findById(checkInId)).thenReturn(Optional.of(c));

        service.updateCheckInStatus(checkInId, CheckIn.Status.DENIED, "bad item");

        assertEquals(CheckIn.Status.DENIED, c.getStatus());
        assertEquals(0, c.getRewardPoints());
        assertEquals(100, u.getPointBalance()); // unchanged

        verify(userRepository, never()).save(any());
        verify(checkInRepository).save(c);
    }

    @Test
    void updateCheckInStatus_whenCheckInNotFound_throws() {
        when(checkInRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.updateCheckInStatus(404L, CheckIn.Status.APPROVED, "x"));

        verify(checkInRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    // ---------------- findById ----------------

    @Test
    void findById_delegatesToAdminrepo() {
        Admin a = admin(1, "admin1");
        when(adminrepo.findById(1L)).thenReturn(Optional.of(a));

        Optional<Admin> result = service.findById(1L);

        assertTrue(result.isPresent());
        verify(adminrepo).findById(1L);
    }

    // ---------------- reviewCheckIn ----------------

    @Test
    void reviewCheckIn_returnsCheckIn() {
        long id = 20L;
        CheckIn c = new CheckIn();

        when(checkInRepository.findByIdWithDetails(id)).thenReturn(Optional.of(c));

        CheckIn result = service.reviewCheckIn(id);

        assertSame(c, result);
        verify(checkInRepository).findByIdWithDetails(id);
    }

    @Test
    void reviewCheckIn_whenNotFound_throws() {
        long id = 21L;
        when(checkInRepository.findByIdWithDetails(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.reviewCheckIn(id));
        verify(checkInRepository).findByIdWithDetails(id);
    }
}



