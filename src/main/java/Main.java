
//import main.java.InvSys;

/**
 * Created by rajan on 6/27/15.
 */
public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY_SYSTEM";

    public static void main(String[] args) {
       // InvSys inv1 = new InvSys();

        UserInterface u=new UserInterface();
        u.start();
        //int invID;
        //inv1.addInventory("Dli");
        //inv1.addNewItem(2,"pencil",2,300);
        //inv1.getOrder(1,122,2);
        // inv1.cancelOrder(5,2);
        //inv1.updateQuantity(1,2,300);


    }


}

