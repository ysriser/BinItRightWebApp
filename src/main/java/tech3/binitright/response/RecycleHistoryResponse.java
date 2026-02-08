package tech3.binitright.response;

import java.time.LocalDateTime;

public final class RecycleHistoryResponse {

    private final String categoryName;
    private final String categoryIcon;
    private final LocalDateTime date;
    private final int quantity;

    public RecycleHistoryResponse(final String categoryName,
                                  final String categoryIcon,
                                  final LocalDateTime date,
                                  final Integer quantity
    ) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.date = date;
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }
}
