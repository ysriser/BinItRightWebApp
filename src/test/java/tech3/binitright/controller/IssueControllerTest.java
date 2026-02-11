package tech3.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Issue;
import tech3.binitright.model.Issue.IssueStatus;
import tech3.binitright.model.Issue.IssueCategory;
import tech3.binitright.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class IssueControllerTest {

    private IssueController controller;
    private IssueInterface issueService;
    private AdminInterface adminService;

    @BeforeEach
    void setUp() {
        controller = new IssueController();
        issueService = Mockito.mock(IssueInterface.class);
        adminService = Mockito.mock(AdminInterface.class);

        ReflectionTestUtils.setField(controller, "issueService", issueService);
        ReflectionTestUtils.setField(controller, "adminService", adminService);
    }

    @Test
    void viewIssues_ReturnsIssuesViewAndPopulatesModel() {

        Model model = new ExtendedModelMap();
        List<Issue> mockIssues = List.of(new Issue(IssueCategory.BinIssues, "Broken Lid", IssueStatus.NEW, new User(), null));

        when(issueService.getAllIssues()).thenReturn(mockIssues);
        when(issueService.countByStatus(IssueStatus.NEW)).thenReturn(1L);
        when(issueService.countByStatus(IssueStatus.IN_PROGRESS)).thenReturn(0L);
        when(issueService.countByStatus(IssueStatus.RESOLVED)).thenReturn(0L);

        // Act
        String viewName = controller.viewIssues(model);

        // Assert
        assertEquals("issues", viewName);
        assertEquals(mockIssues, model.getAttribute("issues"));
        assertEquals(1, model.getAttribute("totalCount"));
        assertEquals(1L, model.getAttribute("newCount"));
        assertEquals("/admin/issues", model.getAttribute("currentPath"));
    }

    @Test
    void viewIssue_ReturnsSingleIssueView() {

        Model model = new ExtendedModelMap();
        Issue mockIssue = new Issue(IssueCategory.AppProblems, "Login bug", IssueStatus.NEW, new User(), null);
        when(issueService.getIssueById(1L)).thenReturn(mockIssue);

        // Act
        String viewName = controller.viewIssue(1L, model);

        // Assert
        assertEquals("admin-issue-resolve", viewName);
        assertEquals(mockIssue, model.getAttribute("issue"));
    }

    @Test
    void markInProgress_RedirectsToIssuesList() {

        Authentication auth = Mockito.mock(Authentication.class);
        Admin mockAdmin = new Admin();
        mockAdmin.setId(5L);

        when(auth.getName()).thenReturn("admin_user");
        when(adminService.getSingleAdminByUsername("admin_user")).thenReturn(mockAdmin);

        // Act
        String viewName = controller.markInProgress(1L, auth);

        // Assert
        assertEquals("redirect:/admin/issues", viewName);
        verify(issueService).markInProgress(1L, 5L);
    }

    @Test
    void resolveIssue_CallsServiceAndRedirects() {

        Authentication auth = Mockito.mock(Authentication.class);
        Admin mockAdmin = new Admin();
        mockAdmin.setId(5L);

        when(auth.getName()).thenReturn("admin_user");
        when(adminService.getSingleAdminByUsername("admin_user")).thenReturn(mockAdmin);

        // Act
        String viewName = controller.resolveIssue(10L, auth);

        // Assert
        assertEquals("redirect:/admin/issues", viewName);
        verify(issueService).resolveIssue(10L, 5L);
    }
}
