package main.java;

/**
 * Created by rajan on 6/27/15.
 */
public interface InventoryManager {

    public void addInventory(String location);
    public void addNewItem(int inventoryID, String itemName, int price, int qty);
    public void updateQuantity(int itemID,int invID, int upQty);

}
