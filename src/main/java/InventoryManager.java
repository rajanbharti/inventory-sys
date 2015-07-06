package main.java;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by rajan on 6/27/15.
 */
public class InventoryManager {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/inventory";

    protected Connection conn;
    protected PreparedStatement p1;
    protected String userName;
    protected String password;

    protected void authenticate() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter username");
            userName = s.next();
            System.out.println("Enter Password");
            password = s.next();
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL, userName, password);
        } catch (SQLException e) {
            System.out.println("Enter correct user name and password");
            authenticate();
        }
    }

    public void addInventory(String location) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, userName, password);
            String sqlAddInv = "INSERT INTO inv_list (location) VALUES (?)";
            p1 = conn.prepareStatement(sqlAddInv);
            p1.setString(1, location);
            p1.execute();

            String sqlGetInvId="SELECT inv_id FROM inv_list ORDER BY inv_id DESC LIMIT 1";
            p1=conn.prepareStatement(sqlGetInvId);
            ResultSet rs=p1.executeQuery();
            rs.next();
            System.out.println("Inventory ID: "+ rs.getInt("inv_id"));
            p1.close();
            conn.close();
        } catch (Exception e) {

        }
    }

    public void addNewItem(int inventoryId, String itemName, int price) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, userName, password);

            String sqlAddItem = "INSERT INTO item_list (name,price) VALUES (?,?)";
            p1 = conn.prepareStatement(sqlAddItem);
            p1.setString(1, itemName);
            p1.setInt(2, price);
            p1.executeUpdate();

            String getItemId = "SELECT item_id FROM item_list ORDER BY item_id DESC LIMIT 1";
            p1 = conn.prepareStatement(getItemId);
            ResultSet rs = p1.executeQuery();
            rs.next();
            int itemId = rs.getInt("item_id");
            System.out.println("Item ID: " + itemId);

            String updateInv = "INSERT INTO inv_info (inv_id,item_id,qty) VALUES (?,?,?)";
            p1 = conn.prepareStatement(updateInv);
            p1.setInt(1, inventoryId);
            p1.setInt(2, itemId);
            p1.setInt(3, 0);
            p1.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(int itemId, int inventoryId, int upQty) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, userName, password);

            int qty = getQty(itemId, inventoryId);

            String sqlUpdateQty = "INSERT INTO inv_info (qty) VALUES (?) WHERE inv_id=? AND item_id=?";
            p1 = conn.prepareStatement(sqlUpdateQty);
            p1.setInt(1, (qty + upQty));
            p1.setInt(2, inventoryId);
            p1.setInt(3, itemId);
            p1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getQty(int itemId, int inventoryId) throws SQLException {
        String sqlGetQty = "SELECT qty FROM inv_info WHERE inv_id=? AND item_id=?";
        p1 = conn.prepareStatement(sqlGetQty);
        p1.setInt(1, inventoryId);
        p1.setInt(2, itemId);
        ResultSet rs = p1.executeQuery();
        rs.next();
        return rs.getInt("qty");
    }
}
