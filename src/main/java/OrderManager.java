package main.java;

/**
 * Created by rajan on 6/26/15.
 */
public interface OrderManager {
    public void getOrder(int itemID, int qty, int inventoryID);

    public void cancelOrder(int id,int invID);
}
