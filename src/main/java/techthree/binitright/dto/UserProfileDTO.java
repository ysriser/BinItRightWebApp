package techthree.binitright.dto;

public class UserProfileDTO {
    private String name;
    private int pointBalance;
    private String equippedAvatarName; // The String we'll map to the drawable
    private int totalRecycled;
    private String aiSummary;
    private int totalAchievements;
    private float carbonEmissionSaved;


    public UserProfileDTO() {}

    public UserProfileDTO(String name, int pointBalance, String equippedAvatarName, int totalRecycled,  String aiSummary,  int totalAchievements, float carbonEmissionSaved) {
        this.name = name;
        this.pointBalance = pointBalance;
        this.equippedAvatarName = equippedAvatarName;
        this.totalRecycled =  totalRecycled;
        this.aiSummary = aiSummary;
        this.totalAchievements = totalAchievements;
        this.carbonEmissionSaved = carbonEmissionSaved;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPointBalance() { return pointBalance; }
    public void setPointBalance(int pointBalance) { this.pointBalance = pointBalance; }

    public String getEquippedAvatarName() { return equippedAvatarName; }
    public void setEquippedAvatarName(String equippedAvatarName) { this.equippedAvatarName = equippedAvatarName; }

    public int getTotalRecycled() { return totalRecycled; }
    public void setTotalRecycled(int totalRecycled) {this.totalRecycled = totalRecycled; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public int getTotalAchievements() { return totalAchievements; }
    public void setTotalAchievements(int totalAchievements) { this.totalAchievements = totalAchievements; }

    public float getCarbonEmissionSaved() { return carbonEmissionSaved; }
    public void setCarbonEmissionSaved() { this.carbonEmissionSaved = carbonEmissionSaved; }
}
