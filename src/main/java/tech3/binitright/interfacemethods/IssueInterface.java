package tech3.binitright.interfacemethods;

import java.util.Collection;
import java.util.List;

import tech3.binitright.model.Issue;
import tech3.binitright.request.IssueCreateRequest;

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
