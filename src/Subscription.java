import java.sql.PreparedStatement;

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
        
    public static void insert(User user, int pkg_id) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("insert into subscription (cust_id, pkg_id) values (?, ?)");
        st.setInt(1, user.id);
        st.setInt(2, pkg_id);
        st.execute();
        client.close();
    }
}
