package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class IssueTest {
    @Test
    void settersAndGetters_shouldWork() {
        Issue issue = new Issue();

        User raisedBy = new User();
        Admin resolvedBy = new Admin();

        issue.setIssueId(1L);
        issue.setIssueCategory(Issue.IssueCategory.AppProblems);
        issue.setDescription("App crashes on login");
        issue.setStatus(Issue.IssueStatus.NEW);
        issue.setRaisedBy(raisedBy);
        issue.setResolvedBy(resolvedBy);

        assertEquals(1L, issue.getIssueId());
        assertEquals(Issue.IssueCategory.AppProblems, issue.getIssueCategory());
        assertEquals("App crashes on login", issue.getDescription());
        assertEquals(Issue.IssueStatus.NEW, issue.getStatus());
        assertSame(raisedBy, issue.getRaisedBy());
        assertSame(resolvedBy, issue.getResolvedBy());
    }

    @Test
    void constructor_shouldSetFields() {
        User raisedBy = new User();
        Admin resolvedBy = new Admin();

        Issue issue = new Issue(
                Issue.IssueCategory.BinIssues,
                "Bin is full",
                Issue.IssueStatus.IN_PROGRESS,
                raisedBy,
                resolvedBy
        );

        assertEquals(Issue.IssueCategory.BinIssues, issue.getIssueCategory());
        assertEquals("Bin is full", issue.getDescription());
        assertEquals(Issue.IssueStatus.IN_PROGRESS, issue.getStatus());
        assertSame(raisedBy, issue.getRaisedBy());
        assertSame(resolvedBy, issue.getResolvedBy());
    }
}
