package main.java;

import main.java.InventoryManager;
import main.java.OrderManager;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by rajan on 6/29/15.
 */
public class InvSys extends InventoryManager implements OrderManager {

    public InvSys() {
        authenticate();
        conn = null;
        p1 = null;
    }

    public void getOrder(int itemID, int orderQty, int inventoryID) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, userName, password);

            int qty = getQty(itemID, inventoryID);
            if (orderQty < qty) {
                placeOrder(itemID, orderQty, inventoryID, qty);
            } else {
                String sqlGetInvCount = "SELECT COUNT(*) FROM inv_list";
                p1 = conn.prepareStatement(sqlGetInvCount);
                ResultSet rs = p1.executeQuery();
                rs.next();
                int invCount = rs.getInt("COUNT");
                if (invCount > 1) {
                    for (int i = 1; i <= invCount; i++) {
                        qty = getQty(itemID, i);
                        if (qty > orderQty) {
                            placeOrder(itemID, orderQty, i, qty);
                            break;
                        }
                    }
                } else {
                    System.out.println("Not Enough Quantity");
                }
            }
            p1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void placeOrder(int itemID, int orderQty, int inventoryID, int qty) throws SQLException {
        String sqlPlaceOrder = "INSERT INTO order_list (item_id,qty,inv_id) VALUES(?,?,?)";
        p1 = conn.prepareStatement(sqlPlaceOrder);
        p1.setInt(1, itemID);
        p1.setInt(2, orderQty);
        p1.setInt(3, inventoryID);
        p1.executeUpdate();

        String sqlGetOrderId = "SELECT order_id FROM order_list ORDER BY order_id DESC LIMIT 1";
        p1 = conn.prepareStatement(sqlGetOrderId);
        ResultSet rs = p1.executeQuery();
        System.out.println("Order ID: " + rs.getInt("order_id"));

        updateQuantity(itemID, inventoryID, (-orderQty));
    }


    public void cancelOrder(int orderID, int inventoryID) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, userName, password);

            String sqlGetOrderInfo = "SELECT item_id,qty FROM order_list WHERE order_id=?";
            p1 = conn.prepareStatement(sqlGetOrderInfo);
            ResultSet rs = p1.executeQuery();
            rs.next();
            int itemId = rs.getInt("item_id");
            int orderQty = rs.getInt("qty");

            String sqlCancelOrder = "DELETE FROM order_list WHERE order_id=?";
            p1 = conn.prepareStatement(sqlCancelOrder);
            p1.setInt(1, orderID);
            p1.execute();

            updateQuantity(itemId, inventoryID, orderQty);

            p1.close();
            conn.close();
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
            System.out.println("Enter correct values");
        }
    }
}
