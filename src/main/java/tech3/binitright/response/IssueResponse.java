package tech3.binitright.response;

public final class IssueResponse {
    private Long issueId;

    public IssueResponse() {}

    public IssueResponse(final Long issueId) {
        this.issueId = issueId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(final Long issueId) {
        this.issueId = issueId;
    }
}
