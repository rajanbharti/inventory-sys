package main.java;

import main.java.InventoryManager;
import main.java.OrderManager;

import java.sql.*;

/**
 * Created by rajan on 6/29/15.
 */
public class InvSys implements OrderManager, InventoryManager {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY_SYSTEM";

    int ListIndex;
    int orderID;
    Connection conn;
    PreparedStatement p1;

    public InvSys() {
        conn = null;
        p1 = null;
    }


    public void addInventory(String location) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "root", "pass");

            String itemList = "iList" + ListIndex;
            String oList = "oList" + ListIndex;
            String invName = "inv" + ListIndex;
            String sqlIList = "CREATE TABLE " + itemList + "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,name VARCHAR(25),price INT)";
            p1 = conn.prepareStatement(sqlIList);
            p1.execute();

            String sqloList = "CREATE TABLE " + oList + "(order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,item_id INT,quantity INT)";
            p1 = conn.prepareStatement(sqloList);
            p1.execute();

            String sqlInvList = "CREATE TABLE " + invName + "(item_id INT,quantity INT)";
            p1 = conn.prepareStatement(sqlInvList);
            p1.execute();

            String sqlInv = "INSERT INTO inventories (inv_name,itemlist_name,orderlist_name,location) VALUES (?,?,?,?)";
            p1 = conn.prepareStatement(sqlInv);
            p1.setString(1, invName);
            p1.setString(2, itemList);
            p1.setString(3, oList);
            p1.setString(4, location);
            p1.execute();
            p1.close();
            conn.close();

        } catch (Exception e) {

        }
    }


    public void addNewItem(int inventoryID, String itemName, int price, int qty) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "root", "pass");

            String invName = "";
            String iListName = "";
            String getInvName = "SELECT inv_name,itemlist_name FROM inventories WHERE inv_id=?";
            p1 = conn.prepareStatement(getInvName);
            p1.setInt(1, inventoryID);
            ResultSet rs = p1.executeQuery();
            while (rs.next()) {
                iListName = rs.getString("itemlist_name");
                invName = rs.getString("inv_name");

            }
            String addItem = "INSERT INTO " + iListName + "(name,price) VALUES (?,?)";
            p1 = conn.prepareStatement(addItem);
            p1.setString(1, itemName);
            p1.setInt(2, price);
            p1.executeUpdate();
            String getItemID = "SELECT id FROM " + iListName + " ORDER BY id DESC LIMIT 1";
            p1 = conn.prepareStatement(getItemID);
            rs = p1.executeQuery();

            String item_id = "";
            rs.next();
            item_id = rs.getString("id");
            String addQty = "INSERT INTO " + invName + "(item_id,quantity) VALUES (?,?)";
            p1 = conn.prepareStatement(addQty);
            p1.setString(1, item_id);
            p1.setInt(2, qty);
            p1.executeUpdate();
            rs.close();
            p1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getOrder(int itemID, int orderQty, int inventoryID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "root", "pass");

            String sqlGetInvName = "SELECT inv_name,orderlist_name FROM inventories WHERE inv_id=?";
            p1 = conn.prepareStatement(sqlGetInvName);
            p1.setInt(1, inventoryID);
            ResultSet rs = p1.executeQuery();
            rs.next();
            String invName = rs.getString("inv_name");
            String oListName = rs.getString("orderlist_name");

            String getQty = "SELECT quantity FROM " + invName + " WHERE item_id=?";
            p1 = conn.prepareStatement(getQty);
            p1.setInt(1, itemID);
            rs = p1.executeQuery();
            rs.next();
            int qty = rs.getInt("quantity");
            if (orderQty <= qty) {
                addOrder(oListName, itemID, orderQty);

                String updateQty = "UPDATE " + invName + " set quantity=? WHERE item_id=?";
                p1 = conn.prepareStatement(updateQty);
                p1.setInt(1, qty - orderQty);
                p1.setInt(2, itemID);
                p1.execute();

            } else {
                String getInvCount = "SELECT COUNT(*) FROM inventories";
                p1 = conn.prepareStatement(getInvCount);
                rs = p1.executeQuery();
                rs.next();
                int invCount = rs.getInt("COUNT(*)");
                for (int i = 1; i <= invCount; i++) {
                    invName = getInvName(i);
                    int itemCount = getItemCount(invName, itemID);
                    if (itemCount > orderQty) {
                        oListName = getOrderList(i);
                        addOrder(oListName, itemID, orderQty);

                        String updateQty = "UPDATE " + invName + " set quantity=? WHERE item_id=?";
                        p1 = conn.prepareStatement(updateQty);
                        p1.setInt(1, qty - orderQty);
                        p1.setInt(2, itemID);
                        p1.execute();

                        break;
                    }
                }

            }
            p1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getInvName(int invID) throws SQLException {
        String sqlGetInvName = "SELECT inv_name FROM inventories WHERE id=?";
        p1 = conn.prepareStatement(sqlGetInvName);
        p1.setInt(1, invID);
        ResultSet rs = p1.executeQuery();
        rs.next();
        return (rs.getString("inv_name"));
    }

    private int getItemCount(String invName, int item_iD) throws SQLException {
        String sqlGetCount = "SELECT quantity FROM " + invName + " item_id=?";
        p1 = conn.prepareStatement(sqlGetCount);
        p1.setInt(1, item_iD);
        ResultSet rs = p1.executeQuery();
        rs.next();
        return (rs.getInt("quantity"));
    }

    private void addOrder(String oListName, int itemID, int orderQty) throws SQLException {
        String sqlAddOrder = "INSERT INTO " + oListName + "(item_id,quantity) VALUES (?,?)";
        p1 = conn.prepareStatement(sqlAddOrder);
        p1.setInt(1, itemID);
        p1.setInt(2, orderQty);
        p1.executeUpdate();

        String getOrderID = "SELECT order_id FROM " + oListName + " ORDER BY order_id DESC LIMIT 1";
        p1 = conn.prepareStatement(getOrderID);
        ResultSet rs = p1.executeQuery();
        rs.next();
        int order_id = rs.getInt("order_id");
        System.out.println("Order ID: " + order_id);
    }

    private String getOrderList(int invID) throws SQLException {
        String sqlGetOrderList = "SELECT orderlist_name FROM inventories WHERE id=?";
        p1 = conn.prepareStatement(sqlGetOrderList);
        p1.setInt(1, invID);
        ResultSet rs = p1.executeQuery();
        rs.next();
        return (rs.getString("orderlist_name"));
    }


    public void updateQuantity(int itemID, int inventoryID, int upQty) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "root", "pass");

            String getInvName = "SELECT inv_name FROM inventories WHERE inv_id=?";
            p1 = conn.prepareStatement(getInvName);
            p1.setInt(1, inventoryID);
            ResultSet rs = p1.executeQuery();
            rs.next();
            String invName = rs.getString("inv_name");
            System.out.println(invName);

            String getQty = "SELECT quantity FROM " + invName + " WHERE item_id=?";
            p1 = conn.prepareStatement(getQty);
            p1.setInt(1, itemID);
            rs = p1.executeQuery();
            rs.next();
            int qty = rs.getInt("quantity");

            String updateQty = "UPDATE " + invName + " SET quantity=? WHERE ITEM_ID=?";
            p1 = conn.prepareStatement(updateQty);
            p1.setInt(1, qty + upQty);
            p1.setInt(2, itemID);
            p1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cancelOrder(int orderID, int inventoryID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "root", "pass");

            String getOrderList = ("SELECT orderlist_name,inv_name FROM inventories WHERE inv_id=?");
            p1 = conn.prepareStatement(getOrderList);
            p1.setInt(1, inventoryID);
            ResultSet rs = p1.executeQuery();
            rs.next();
            String oListName = rs.getString("orderlist_name");
            String invName = rs.getString("inv_name");

            String getItemId = "SELECT item_id FROM " + oListName + " WHERE order_id=?";
            p1 = conn.prepareStatement(getItemId);
            p1.setInt(1, orderID);
            rs = p1.executeQuery();
            rs.next();
            int itemId = rs.getInt("item_id");

            String getOrderQty = "SELECT quantity FROM " + oListName + " WHERE order_id=?";
            p1 = conn.prepareStatement(getOrderQty);
            p1.setInt(1, orderID);
            rs = p1.executeQuery();
            rs.next();
            int orderQty = rs.getInt("quantity");
            System.out.println(orderQty);

            String deleteOrder = "DELETE FROM " + oListName + " WHERE order_id=?";
            p1 = conn.prepareStatement(deleteOrder);
            p1.setInt(1, orderID);
            p1.executeUpdate();

            String getQty = "SELECT quantity FROM " + invName + " WHERE item_id=?";
            p1 = conn.prepareStatement(getQty);
            p1.setInt(1, itemId);
            rs = p1.executeQuery();
            rs.next();
            int qty = rs.getInt("quantity");
            System.out.println(qty);

            String updateQty = "UPDATE " + invName + " SET quantity=? WHERE item_id=?";
            p1 = conn.prepareStatement(updateQty);
            p1.setInt(1, (orderQty + qty));
            p1.setInt(2, itemId);
            p1.executeUpdate();

            p1.close();
            conn.close();
        } catch (Exception e) {

        }

    }


}
