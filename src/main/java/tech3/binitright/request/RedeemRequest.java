package tech3.binitright.request;

public class RedeemRequest {
    private Long userId;
    private Long accessoriesId;

    public RedeemRequest() {}

    public RedeemRequest(final Long userId, final Long accessoriesId) {
        this.userId = userId;
        this.accessoriesId = accessoriesId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(final Long userId) { this.userId = userId; }

    public Long getAccessoriesId() { return accessoriesId; }
    public void setAccessoriesId(final Long accessoriesId) { this.accessoriesId = accessoriesId; }
}

