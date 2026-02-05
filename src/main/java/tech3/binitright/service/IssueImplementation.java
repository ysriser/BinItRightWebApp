package tech3.binitright.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Issue;
import tech3.binitright.model.User;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.IssueRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.request.IssueCreateRequest;
import java.util.List;

@Service
@Transactional
public class IssueImplementation implements IssueInterface {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public Issue createIssue(IssueCreateRequest req) {

        User user = userRepository.findById(req.raisedByUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Issue issue = new Issue(
                Issue.IssueCategory.valueOf(req.issueCategory()),
                req.description(),
                Issue.IssueStatus.NEW,
                user,
                null
        );
        return issueRepository.save(issue);
    }

    /** Dashboard preview */
    @Override
    @Transactional(readOnly = true)
    public List<Issue> getLatestIssuesForDashboard() {
        return issueRepository.findTop5WithRaisedBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAllIssues() {
        List<Issue> list = issueRepository.findAllWithRaisedBy();
        System.out.println("ISSUES FOUND: " + list.size());
        return list;
    }

    @Override
    @Transactional
    public Issue getIssueById(Long id) {
        return issueRepository.findByIdWithRaisedBy(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Issue not found"
                        )
                );
    }

    @Override
    @Transactional
    public long countByStatus(Issue.IssueStatus status) {
        return issueRepository.countByStatus(status);
    }

    @Override
    @Transactional
    public long getTotalIssueCount() {
        return issueRepository.count();
    }

    @Override
    @Transactional
    public void resolveIssue(Long issueId, Long adminId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));

        issue.setStatus(Issue.IssueStatus.RESOLVED);
        issue.setResolvedBy(admin);
        issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void markInProgress(Long issueId, Long adminId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        issue.setStatus(Issue.IssueStatus.IN_PROGRESS);
        issue.setResolvedBy(null);
        issueRepository.save(issue);
    }
}



