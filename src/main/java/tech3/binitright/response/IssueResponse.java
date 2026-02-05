package tech3.binitright.response;

public class IssueResponse {
    private Long issueId;

    public IssueResponse() {}

    public IssueResponse(Long issueId) {
        this.issueId = issueId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}
