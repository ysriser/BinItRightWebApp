package techthree.binitright.dto;

public class LeaderboardDTO {
    private Long userId;
    private String username;
    private Long totalQuantity;

    public LeaderboardDTO(final Long userId, final String username, final Long totalQuantity) {
        this.userId = userId;
        this.username = username;
        this.totalQuantity = totalQuantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(final Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}