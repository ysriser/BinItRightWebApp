package tech3.binitright.dto;

public final class UserProfileDTO {
    private String name;
    private int pointBalance;
    private String equippedAvatarName; // The String we'll map to the drawable
    private int totalRecycled;
    private String aiSummary;
    private int totalAchievements;
    private float carbonEmissionSaved;


    public UserProfileDTO() {}

    public UserProfileDTO(final String name, final int pointBalance,
    		final String equippedAvatarName, final int totalRecycled,
    		final String aiSummary,  final int totalAchievements,
    		final float carbonEmissionSaved) {
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
    public void setName(final String name) { this.name = name; }

    public int getPointBalance() { return pointBalance; }
    public void setPointBalance(final int pointBalance) { this.pointBalance = pointBalance; }

    public String getEquippedAvatarName() { return equippedAvatarName; }
    public void setEquippedAvatarName(final String equippedAvatarName) { this.equippedAvatarName = equippedAvatarName; }

    public int getTotalRecycled() { return totalRecycled; }
    public void setTotalRecycled(final int totalRecycled) {this.totalRecycled = totalRecycled; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(final String aiSummary) { this.aiSummary = aiSummary; }

    public int getTotalAchievements() { return totalAchievements; }
    public void setTotalAchievements(final int totalAchievements) { this.totalAchievements = totalAchievements; }

    public float getCarbonEmissionSaved() { return carbonEmissionSaved; }
    public void setCarbonEmissionSaved() { this.carbonEmissionSaved = carbonEmissionSaved; }
}
