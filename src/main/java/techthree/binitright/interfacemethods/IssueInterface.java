package techthree.binitright.interfacemethods;

import techthree.binitright.model.Issue;
import techthree.binitright.request.IssueCreateRequest;

import java.util.Collection;
import java.util.List;

public interface IssueInterface {
    public Issue createIssue(IssueCreateRequest req, Long userId);
    public List<Issue> getAllIssues();
    public List<Issue> getLatestIssuesForDashboard();
    public long getTotalIssueCount();
    public long countByStatus(Issue.IssueStatus status);
    public void markInProgress(Long issueId, Long adminId);
    public void resolveIssue(Long issueId, Long adminId);
    Issue getIssueById(Long issueId);

    Collection<Issue> findAll();

    List<Issue> saveAll(List<Issue> issues);
}
