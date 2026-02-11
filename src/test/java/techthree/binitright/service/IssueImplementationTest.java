package techthree.binitright.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import techthree.binitright.model.Admin;
import techthree.binitright.model.Issue;
import techthree.binitright.model.User;
import techthree.binitright.repository.AdminRepository;
import techthree.binitright.repository.IssueRepository;
import techthree.binitright.repository.UserRepository;
import techthree.binitright.request.IssueCreateRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueImplementationTest {
    @Mock private IssueRepository issueRepo;
    @Mock private UserRepository userRepository;
    @Mock private AdminRepository adminRepository;

    private IssueImplementation service;
    private FakeAchievementImplementation fakeAchievements;

    // -------- Fake AchievementImplementation (no Mockito, no ByteBuddy) ----------
    static class FakeAchievementImplementation extends AchievementImplementation {
        Long lastUserId;
        Long lastAchievementId;

        FakeAchievementImplementation() {
            super(null, null, null);
        }

        @Override
        public void unlockAchievement(Long userId, Long achievementId) {
            this.lastUserId = userId;
            this.lastAchievementId = achievementId;
        }
    }

    @BeforeEach
    void setUp() {
        service = new IssueImplementation();
        fakeAchievements = new FakeAchievementImplementation();

        ReflectionTestUtils.setField(service, "issueRepo", issueRepo);
        ReflectionTestUtils.setField(service, "userRepository", userRepository);
        ReflectionTestUtils.setField(service, "adminRepository", adminRepository);
        ReflectionTestUtils.setField(service, "achievementImplementation", fakeAchievements);
    }

    // -------- helpers ----------
    private User user(Long id) {
        User u = new User();
        u.setId(id);
        return u;
    }

    private Admin admin(Long id) {
        Admin a = new Admin();
        a.setId(id);
        return a;
    }

    @Test
    void createIssue_whenUserExists_savesIssue_andUnlocksAchievement9() {
        // given
        Long userId = 10L;
        User u = user(userId);

        IssueCreateRequest req = new IssueCreateRequest(
                "BinIssues",
                "Bin is overflowing",
                null          // 👈 third argument
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(issueRepo.save(any(Issue.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Issue saved = service.createIssue(req, userId);

        // then
        assertNotNull(saved);
        assertEquals(Issue.IssueStatus.NEW, saved.getStatus());
        assertEquals("Bin is overflowing", saved.getDescription());
        assertEquals(u, saved.getRaisedBy());
        assertNull(saved.getResolvedBy());

        // achievement unlock called
        assertEquals(userId, fakeAchievements.lastUserId);
        assertEquals(9L, fakeAchievements.lastAchievementId);

        // verify correct issue object saved
        ArgumentCaptor<Issue> captor = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepo).save(captor.capture());
        Issue toSave = captor.getValue();

        assertEquals(Issue.IssueStatus.NEW, toSave.getStatus());
        assertEquals(u, toSave.getRaisedBy());
        assertEquals(Issue.IssueCategory.BIN_ISSUES, toSave.getIssueCategory());
    }

    @Test
    void createIssue_whenUserMissing_throwsEntityNotFound() {
        Long userId = 99L;
        IssueCreateRequest req = new IssueCreateRequest("BinIssues", "x", null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.createIssue(req, userId));

        verify(issueRepo, never()).save(any());
        assertNull(fakeAchievements.lastAchievementId);
    }

    @Test
    void getLatestIssuesForDashboard_returnsRepoTop5() {
        when(issueRepo.findTop5WithRaisedBy()).thenReturn(List.of(new Issue(), new Issue()));

        List<Issue> res = service.getLatestIssuesForDashboard();

        assertEquals(2, res.size());
        verify(issueRepo).findTop5WithRaisedBy();
    }

    @Test
    void getAllIssues_returnsRepoAllWithRaisedBy() {
        when(issueRepo.findAllWithRaisedBy()).thenReturn(List.of(new Issue()));

        List<Issue> res = service.getAllIssues();

        assertEquals(1, res.size());
        verify(issueRepo).findAllWithRaisedBy();
    }

    @Test
    void findAll_returnsRepoFindAll() {
        when(issueRepo.findAll()).thenReturn(List.of(new Issue(), new Issue(), new Issue()));

        Collection<Issue> res = service.findAll();

        assertEquals(3, res.size());
        verify(issueRepo).findAll();
    }

    @Test
    void saveAll_delegatesToRepo() {
        List<Issue> input = List.of(new Issue(), new Issue());
        when(issueRepo.saveAll(input)).thenReturn(input);

        List<Issue> res = service.saveAll(input);

        assertEquals(2, res.size());
        verify(issueRepo).saveAll(input);
    }

    @Test
    void getIssueById_whenFound_returnsIssue() {
        Issue issue = new Issue();
        when(issueRepo.findByIdWithRaisedBy(1L)).thenReturn(Optional.of(issue));

        Issue res = service.getIssueById(1L);

        assertSame(issue, res);
        verify(issueRepo).findByIdWithRaisedBy(1L);
    }

    @Test
    void getIssueById_whenMissing_throws404() {
        when(issueRepo.findByIdWithRaisedBy(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex =
                assertThrows(ResponseStatusException.class, () -> service.getIssueById(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Issue not found"));
    }

    @Test
    void countByStatus_returnsRepoCount() {
        when(issueRepo.countByStatus(Issue.IssueStatus.NEW)).thenReturn(5L);

        long res = service.countByStatus(Issue.IssueStatus.NEW);

        assertEquals(5L, res);
        verify(issueRepo).countByStatus(Issue.IssueStatus.NEW);
    }

    @Test
    void getTotalIssueCount_returnsRepoCount() {
        when(issueRepo.count()).thenReturn(99L);

        long res = service.getTotalIssueCount();

        assertEquals(99L, res);
        verify(issueRepo).count();
    }

    @Test
    void resolveIssue_whenIssueAndAdminExist_setsResolved_andSaves() {
        Issue issue = new Issue();
        issue.setStatus(Issue.IssueStatus.NEW);

        Admin a = admin(7L);

        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));
        when(adminRepository.findById(7L)).thenReturn(Optional.of(a));
        when(issueRepo.save(any(Issue.class))).thenAnswer(inv -> inv.getArgument(0));

        service.resolveIssue(1L, 7L);

        assertEquals(Issue.IssueStatus.RESOLVED, issue.getStatus());
        assertEquals(a, issue.getResolvedBy());
        verify(issueRepo).save(issue);
    }

    @Test
    void resolveIssue_whenIssueMissing_throws() {
        when(issueRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.resolveIssue(1L, 7L));

        verify(issueRepo, never()).save(any());
    }

    @Test
    void resolveIssue_whenAdminMissing_throws() {
        Issue issue = new Issue();
        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));
        when(adminRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.resolveIssue(1L, 7L));

        verify(issueRepo, never()).save(any());
    }

    @Test
    void markInProgress_setsInProgress_clearsResolvedBy_saves() {
        Issue issue = new Issue();
        issue.setResolvedBy(admin(1L));
        issue.setStatus(Issue.IssueStatus.NEW);

        when(issueRepo.findById(5L)).thenReturn(Optional.of(issue));
        when(issueRepo.save(any(Issue.class))).thenAnswer(inv -> inv.getArgument(0));

        service.markInProgress(5L, 99L);

        assertEquals(Issue.IssueStatus.IN_PROGRESS, issue.getStatus());
        assertNull(issue.getResolvedBy());
        verify(issueRepo).save(issue);
    }
}

