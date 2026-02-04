package tech3.binitright.interfacemethods;

import tech3.binitright.model.Issue;
import tech3.binitright.request.IssueCreateRequest;

import java.util.List;

public interface IssueInterface {
    public Issue createIssue(IssueCreateRequest req);
    public List<Issue> getAllIssues();
    public List<Issue> getLatestIssuesForDashboard();
    public long getTotalIssueCount();
    public long countByStatus(Issue.IssueStatus status);
    public void markInProgress(Long issueId, Long adminId);
    public void resolveIssue(Long issueId, Long adminId);
    Issue getIssueById(Long issueId);
}
