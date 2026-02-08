package tech3.binitright.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Issue;
import tech3.binitright.model.User;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.IssueRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.request.IssueCreateRequest;

@Service
@Transactional
public final class IssueImplementation implements IssueInterface {

    @Autowired
    private IssueRepository issueRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AchievementImplementation achievementImplementation;

    @Override
    @Transactional
    public Issue createIssue(final IssueCreateRequest req, final Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        final Issue issue = new Issue(
                Issue.IssueCategory.valueOf(req.issueCategory()),
                req.description(),
                Issue.IssueStatus.NEW,
                user,
                null
        );
        achievementImplementation.unlockAchievement(user.getId(), 9L);
        return issueRepo.save(issue);
    }

    /** Dashboard preview */
    @Override
    @Transactional(readOnly = true)
    public List<Issue> getLatestIssuesForDashboard() {
        return issueRepo.findTop5WithRaisedBy();
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepo.findAllWithRaisedBy();
    }

    @Override
    public Collection<Issue> findAll() {
        return issueRepo.findAll();
    }

    @Override
    public List<Issue> saveAll(final List<Issue> issues) {
        return issueRepo.saveAll(issues);
    }

    @Override
    @Transactional
    public Issue getIssueById(final Long id) {
        return issueRepo.findByIdWithRaisedBy(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Issue not found"
                        )
                );
    }

    @Override
    @Transactional
    public long countByStatus(final Issue.IssueStatus status) {
        return issueRepo.countByStatus(status);
    }

    @Override
    @Transactional
    public long getTotalIssueCount() {
        return issueRepo.count();
    }

    @Override
    @Transactional
    public void resolveIssue(final Long issueId, final Long adminId) {
        final Issue issue = issueRepo.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        final Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));

        issue.setStatus(Issue.IssueStatus.RESOLVED);
        issue.setResolvedBy(admin);
        issueRepo.save(issue);
    }

    @Override
    @Transactional
    public void markInProgress(final Long issueId, final Long adminId) {
        final Issue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setStatus(Issue.IssueStatus.INUPROGRESS);
        issue.setResolvedBy(null);
        issueRepo.save(issue);
    }
}



