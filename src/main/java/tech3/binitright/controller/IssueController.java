package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    public void setIssueService(IssueImplementation issueserviceImp) {
        this.issueService = issueserviceImp;
    }

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
    }


    @GetMapping
    public String viewIssues(Model model) {

        List<Issue> issues = issueService.getAllIssues();

        model.addAttribute("issues", issues);
        model.addAttribute("totalCount", issues.size());
        model.addAttribute("newCount",
                issueService.countByStatus(IssueStatus.NEW));
        model.addAttribute("inProgressCount",
                issueService.countByStatus(IssueStatus.IN_PROGRESS));
        model.addAttribute("resolvedCount",
                issueService.countByStatus(IssueStatus.RESOLVED));

        model.addAttribute("currentPath", "/admin/issues");

        return "issues";
    }

    /* VIEW SINGLE ISSUE */
    @GetMapping("/{id}")
    public String viewIssue(@PathVariable Long id, Model model) {

        Issue issue = issueService.getIssueById(id);

        model.addAttribute("issue", issue);
        model.addAttribute("currentPath", "/admin/issues");

        return "admin-issue-resolve";
    }

    /* MARK IN PROGRESS */
    @PostMapping("/{id}/in-progress")
    public String markInProgress(@PathVariable Long id,
                                 Authentication authentication) {

        String username = authentication.getName(); // from JWT
        Admin admin = adminService.getSingleAdminByUsername(username);

        issueService.markInProgress(id, admin.getId());
        return "redirect:/admin/issues";
    }

    /* RESOLVE ISSUE */
    @PostMapping("/{id}/resolve")
    public String resolveIssue(@PathVariable Long id,
                               Authentication authentication) {

        String username = authentication.getName();
        Admin admin = adminService.getSingleAdminByUsername(username);

        issueService.resolveIssue(id, admin.getId());
        return "redirect:/admin/issues";
    }


}
