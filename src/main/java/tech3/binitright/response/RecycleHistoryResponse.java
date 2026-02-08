package tech3.binitright.response;

import java.time.LocalDateTime;

public final class RecycleHistoryResponse {

    private String categoryName;
    private String categoryIcon;
    private LocalDateTime date;
    private int quantity;

    public RecycleHistoryResponse(String categoryName,
                                  String categoryIcon,
                                  LocalDateTime date,
                                  Integer quantity
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
