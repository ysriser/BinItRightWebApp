package tech3.binitright.response;

import tech3.binitright.model.User;

public class UserProfileResponse {
    private Long id;
    private Integer pointBalance;

    public UserProfileResponse() {
    }
        // Add this constructor
        public UserProfileResponse(User user) {
            this.id = user.getId();
            this.pointBalance = user.getPointBalance();
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getPointBalance() {
            return pointBalance;
        }

        public void setPointBalance(Integer pointBalance) {
            this.pointBalance = pointBalance;
        }
}
