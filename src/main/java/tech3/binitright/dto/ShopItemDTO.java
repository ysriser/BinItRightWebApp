package tech3.binitright.dto;

public final class ShopItemDTO {
    private Long accessoriesId;
    private String name;
    private int requiredPoints;
    private boolean owned;
    private boolean equipped;

    public ShopItemDTO() {
    }

    // 修复了 LineLengthCheck 报错
    public ShopItemDTO(final Long accessoriesId,
                       final String name,
                       final int requiredPoints,
                       final boolean owned,
                       final boolean equipped) {
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