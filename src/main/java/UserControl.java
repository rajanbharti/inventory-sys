
import main.java.InventoryControl;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by rajan on 7/1/15.
 */
public class UserControl {

    Scanner s;
    int invId;
    InventoryControl inv1;

    public void start() throws SQLException {
        inv1 = new InventoryControl();
        s = new Scanner(System.in);
        displayMenu();
    }

    private void displayMenu() throws SQLException {
        try {
            System.out.println("Enter your inventory ID(default 1)");
            invId = s.nextInt();
            System.out.println("Enter 1 to manage inventory, 2 to manage Order,3 to exit ");
            System.out.println("Add a new inventory, if you haven't added one");

            int choice = s.nextInt();
            switch (choice) {
                case 1:
                    manageInv();
                    break;
                case 2:
                    manageOrder();
                    break;
                case 3:
                    break;
                default:
                    displayMenu();
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter number only");
            start();
        }
    }

    private void manageOrder() throws SQLException {
        System.out.println("Enter 1 to place order, 2 to cancel, 3 to go back");

        try {
            int choice = s.nextInt();
            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    cancelOrder();
                    break;
                case 3:
                    start();
                    break;
                default:
                    manageOrder();
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter number");
            manageOrder();
        }
        manageOrder();
    }

    private void placeOrder() throws SQLException {
        System.out.println("Enter Item ID");
        int itemID = s.nextInt();
        System.out.println("Enter quantity");
        int qty = s.nextInt();
        inv1.getOrder(itemID, qty, invId);

    }

    private void cancelOrder() {
        System.out.println("Enter order ID");
        int orderId = s.nextInt();
        inv1.cancelOrder(orderId, invId);
    }

    private void manageInv() throws SQLException {
        System.out.println("Enter 1 to add inventory, 2 to add new item, 3 to update quantity, 4 to go back");
        try {
            int choice = s.nextInt();
            switch (choice) {
                case 1:
                    addInv();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    updateQty();
                    break;
                case 4:
                    start();
                    break;
                default:
                    manageInv();
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter number only");
            manageInv();
        }
        manageInv();
    }

    private void addInv() {
        System.out.println("Enter location");
        String location = s.next();
        inv1.addInventory(location);
    }

    private void addItem() {
        System.out.println("Enter item name");
        String name = s.next();
        System.out.println("Enter price");
        int price = s.nextInt();
        System.out.println("Enter quantity");
        int qty = s.nextInt();
        inv1.addNewItem(invId, name, price, qty);
    }

    private void updateQty() {
        System.out.println("Enter Item ID");
        int itemID = s.nextInt();
        System.out.println("Enter quantity");
        int qty = s.nextInt();
        inv1.updateQuantity(itemID, invId, qty);
    }
}
