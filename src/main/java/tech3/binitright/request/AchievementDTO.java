package tech3.binitright.request;

public class AchievementDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final String criteria;
    private final String badgeIconUrl;
    private final boolean isUnlocked;

    public AchievementDTO(final Long id, final String name, 
    		final String description, final String criteria, 
    		final String badgeIconUrl, final boolean isUnlocked) {
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