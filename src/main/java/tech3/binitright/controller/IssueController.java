package tech3.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Issue;
import tech3.binitright.model.Issue.IssueStatus;
import tech3.binitright.service.AdminImplementation;
import tech3.binitright.service.IssueImplementation;

@Controller
@RequestMapping("/admin/issues")
public class IssueController {

    @Autowired
    private IssueInterface issueService;

    public void setIssueService(final IssueImplementation issueserviceImp) {
        this.issueService = issueserviceImp;
    }

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(final AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
    }

    @GetMapping
    public String viewIssues(final Model model) {

        final List<Issue> issues = issueService.getAllIssues();

        model.addAttribute("issues", issues);
        model.addAttribute("totalCount", issues.size());
        model.addAttribute("newCount",
                issueService.countByStatus(IssueStatus.NEW));
        model.addAttribute("inProgressCount",
                issueService.countByStatus(IssueStatus.INUPROGRESS));
        model.addAttribute("resolvedCount",
                issueService.countByStatus(IssueStatus.RESOLVED));

        model.addAttribute("currentPath", "/admin/issues");

        return "issues";
    }

    /* VIEW SINGLE ISSUE */
    @GetMapping("/{id}")
    public String viewIssue(@PathVariable final Long id, final Model model) {

        final Issue issue = issueService.getIssueById(id);

        model.addAttribute("issue", issue);
        model.addAttribute("currentPath", "/admin/issues");

        return "admin-issue-resolve";
    }

    /* MARK IN PROGRESS */
    @PostMapping("/{id}/in-progress")
    public String markInProgress(@PathVariable final Long id,
                                 final Authentication authentication) {

        final String username = authentication.getName(); // from JWT
        final Admin admin = adminService.getSingleAdminByUsername(username);

        issueService.markInProgress(id, admin.getId());
        return "redirect:/admin/issues";
    }

    /* RESOLVE ISSUE */
    @PostMapping("/{id}/resolve")
    public String resolveIssue(@PathVariable final Long id,
                               final Authentication authentication) {

        final String username = authentication.getName();
        final Admin admin = adminService.getSingleAdminByUsername(username);

        issueService.resolveIssue(id, admin.getId());
        return "redirect:/admin/issues";
    }


}
