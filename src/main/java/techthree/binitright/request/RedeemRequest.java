package techthree.binitright.request;

public class RedeemRequest {
    private Long userId;
    private Long accessoriesId;

    public RedeemRequest() {}

    public RedeemRequest(Long userId, Long accessoriesId) {
        this.userId = userId;
        this.accessoriesId = accessoriesId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAccessoriesId() { return accessoriesId; }
    public void setAccessoriesId(Long accessoriesId) { this.accessoriesId = accessoriesId; }
}

