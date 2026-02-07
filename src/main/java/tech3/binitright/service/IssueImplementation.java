package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Issue;
import tech3.binitright.model.Issue.IssueStatus;
import tech3.binitright.model.Issue.IssueCategory;
import tech3.binitright.model.User;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.IssueRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.request.IssueCreateRequest;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class IssueImplementation implements IssueInterface {

    @Autowired
    private IssueRepository issueRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private AchievementImplementation achievementImplementation;

    @Override
    public Issue createIssue(IssueCreateRequest request) {
        User user = userRepo.findById(request.raisedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Issue issue = new Issue();
        issue.setIssueCategory(IssueCategory.valueOf(request.issueCategory()));
        issue.setDescription(request.description());
        issue.setStatus(IssueStatus.NEW);
        issue.setRaisedBy(user);

        Issue saved = issueRepo.save(issue);

        achievementImplementation.unlockAchievement(user.getId(), 9L);

        return saved;
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepo.findAllWithRaisedBy();
    }

    @Override
    public Issue getIssueById(Long id) {
        return issueRepo.findByIdWithRaisedBy(id).orElseThrow(() -> new RuntimeException("Issue not found"));
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
    public long countByStatus(IssueStatus status) {
        return issueRepo.countByStatus(status);
    }

    @Override
    public void markInProgress(Long id, Long adminId) {
        Issue issue = getIssueById(id);
        Admin admin = adminRepo.findById(adminId).orElseThrow();
        issue.setStatus(IssueStatus.IN_PROGRESS);
        issue.setResolvedBy(admin);
        issueRepo.save(issue);
    }

    @Override
    public void resolveIssue(Long id, Long adminId) {
        Issue issue = getIssueById(id);
        Admin admin = adminRepo.findById(adminId).orElseThrow();
        issue.setStatus(IssueStatus.RESOLVED);
        issue.setResolvedBy(admin);
        issueRepo.save(issue);
    }

    @Override
    public long getTotalIssueCount() {
        return issueRepo.count();
    }

    @Override
    public List<Issue> getLatestIssuesForDashboard() {
        return issueRepo.findTop5WithRaisedBy();
    }
}