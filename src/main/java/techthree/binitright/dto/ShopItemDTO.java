package techthree.binitright.dto;

public class ShopItemDTO {
    private Long accessoriesId;
    private String name;
    private int requiredPoints;
    private boolean owned;
    private boolean equipped;

    public ShopItemDTO() {}

    public ShopItemDTO(Long accessoriesId, String name, int requiredPoints, boolean owned, boolean equipped) {
        this.accessoriesId = accessoriesId;
        this.name = name;
        this.requiredPoints = requiredPoints;
        this.owned = owned;
        this.equipped = equipped;
    }

    public Long getAccessoriesId() {
        return accessoriesId;
    }
    public String getName() {
        return name;
    }
    public int getRequiredPoints() {
        return requiredPoints;
    }
    public boolean isOwned() {
        return owned;
    }
    public boolean isEquipped() {
        return equipped;
    }
}

