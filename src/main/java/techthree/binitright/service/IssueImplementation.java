package techthree.binitright.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import techthree.binitright.interfacemethods.IssueInterface;
import techthree.binitright.model.Admin;
import techthree.binitright.model.Issue;
import techthree.binitright.model.User;
import techthree.binitright.repository.AdminRepository;
import techthree.binitright.repository.IssueRepository;
import techthree.binitright.repository.UserRepository;
import techthree.binitright.request.IssueCreateRequest;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class IssueImplementation implements IssueInterface {

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
    public Issue createIssue(IssueCreateRequest req, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Issue issue = new Issue(
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
    public List<Issue> saveAll(List<Issue> issues) {
        return issueRepo.saveAll(issues);
    }

    @Override
    @Transactional
    public Issue getIssueById(Long id) {
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
    public long countByStatus(Issue.IssueStatus status) {
        return issueRepo.countByStatus(status);
    }

    @Override
    @Transactional
    public long getTotalIssueCount() {
        return issueRepo.count();
    }

    @Override
    @Transactional
    public void resolveIssue(Long issueId, Long adminId) {
        Issue issue = issueRepo.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));

        issue.setStatus(Issue.IssueStatus.RESOLVED);
        issue.setResolvedBy(admin);
        issueRepo.save(issue);
    }

    @Override
    @Transactional
    public void markInProgress(Long issueId, Long adminId) {
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setStatus(Issue.IssueStatus.IN_PROGRESS);
        issue.setResolvedBy(null);
        issueRepo.save(issue);
    }
}



