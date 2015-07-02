
//import main.java.InvSys;

/**
 * Created by rajan on 6/27/15.
 */
public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY_SYSTEM";

    public static void main(String[] args) {
        UserInterface u = new UserInterface();
        u.start();
    }
}

