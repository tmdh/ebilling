
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */
public class Subscription {
    int id;
    String name;
    int price, billing_day, bandwidth;
    boolean status;
    
    private Logger logger = Logger.getLogger("User");
    
    public static void insert(DatabaseClient client, User user, int pkg_id) throws Exception {
        PreparedStatement st = client.connection.prepareStatement("insert into subscription (cust_id, pkg_id) values (?, ?)");
        st.setInt(1, user.id);
        st.setInt(2, pkg_id);
        st.execute();
    }
}
