package techthree.binitright.request;

public class AchievementDTO {
    private Long id;
    private String name;
    private String description;
    private String criteria;
    private String badgeIconUrl;
    private boolean isUnlocked;

    public AchievementDTO(Long id, String name, String description, String criteria, String badgeIconUrl, boolean isUnlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.criteria = criteria;
        this.badgeIconUrl = badgeIconUrl;
        this.isUnlocked = isUnlocked;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCriteria() { return criteria; }
    public String getBadgeIconUrl() { return badgeIconUrl; }
    public boolean getIsUnlocked() { return isUnlocked; }
}