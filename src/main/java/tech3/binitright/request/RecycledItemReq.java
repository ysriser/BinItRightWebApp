package tech3.binitright.request;

public class RecycledItemReq {
	 
	private String itemType;
	private int quantity;
	 
	 
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "Items [itemType=" + itemType + ", quantity=" + quantity + "]";
	}
	 
	
	
}
