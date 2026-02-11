package techthree.binitright.request;

public record IssueCreateRequest(
        String issueCategory,
        String description,
        Long raisedByUserId
) {}
