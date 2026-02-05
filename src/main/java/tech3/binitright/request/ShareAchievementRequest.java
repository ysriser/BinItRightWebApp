package tech3.binitright.request;

public class ShareAchievementRequest {
    private Long id;
    private String username;
    private String email;
    private int currentRank;

    public ShareAchievementRequest(Long id, String username, String email, int currentRank) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.currentRank = currentRank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }
}