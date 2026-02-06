package tech3.binitright.dto;

public class UserProfileDTO {
    private String name;
    private int pointBalance;
    private String equippedAvatarName; // The String we'll map to the drawable
    private int totalRecycled;


    public UserProfileDTO() {}

    public UserProfileDTO(String name, int pointBalance, String equippedAvatarName, int totalRecycled) {
        this.name = name;
        this.pointBalance = pointBalance;
        this.equippedAvatarName = equippedAvatarName;
        this.totalRecycled =  totalRecycled;
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
}
