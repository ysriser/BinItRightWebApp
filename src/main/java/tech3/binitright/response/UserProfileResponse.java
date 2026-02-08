package tech3.binitright.response;

import tech3.binitright.model.User;

public final class UserProfileResponse {
    private Long id;
    private Integer pointBalance;

    public UserProfileResponse() {
    }

    public UserProfileResponse(final User user) {
        this.id = user.getId();
        this.pointBalance = user.getPointBalance();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(final Integer pointBalance) {
        this.pointBalance = pointBalance;
    }
}