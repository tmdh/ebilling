
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */
public class User {
    
    String name;
    String email;
    int id;
    DatabaseClient client;
    
    public User(DatabaseClient client, int id) {
        this.id = id;
        this.client = client;
        try {
            PreparedStatement st = client.connection.prepareStatement("select name, email from user where id=?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            this.name = rs.getString("name");
            this.email = rs.getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static User create(DatabaseClient client, String email, String password, String confirmPassword, String name) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("Password fields do not match.");
        }
        
        // Create User in the database
        PreparedStatement st = client.connection.prepareStatement("insert into user(name,email,password) values( ? , ? , ? )");
        st.setString(1, name);
        st.setString(2, email);
        st.setString(3, password);
        st.execute();
        PreparedStatement st2 = client.connection.prepareStatement("select id from user where email = ?");
        st2.setString(1, email);
        ResultSet rs = st2.executeQuery();
        return new User(client, rs.getInt("id"));
    }
    
    public static User find(DatabaseClient client, String email, String password) throws Exception {
        // Find User in the database
        PreparedStatement st = client.connection.prepareStatement("select id from user where email=? and password=?");
        st.setString(1, email);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return new User(client, rs.getInt("id"));
        } else {
            throw new Exception("Email and password do not match");
        }
    }
    
    public int countSubscriptions() throws Exception {
        PreparedStatement st = client.connection.prepareStatement("select count(id) from subscription where cust_id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        return rs.getInt(1);
    }
    
    public int totalBillPerMonth() throws Exception {
        PreparedStatement st = client.connection.prepareStatement("select sum(package.price) from subscription inner join package on package.id=subscription.pkg_id and subscription.cust_id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        return rs.getInt(1);
    }
    
    public Vector<Vector<Object>> subscriptions() throws Exception {
        PreparedStatement st = client.connection.prepareStatement("select subscription.id, package.name, package.price, subscription.billing_day, package.bandwidth, subscription.status from subscription inner join package on package.id=subscription.pkg_id and subscription.cust_id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        Vector<Vector<Object>> v = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> a = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= 6; columnIndex++) {
                if (columnIndex == 6) {
                    a.add(new Boolean(rs.getBoolean(6)));
                } else {
                    a.add(rs.getObject(columnIndex));
                }
            }
            v.add(a);
        }
        return v;
    }
    
    public boolean pay(long trans_id, String provider, int amount, Integer[] subscriptionIDs) throws Exception {
        System.out.println("Finding transaction...");
        PreparedStatement st = client.connection.prepareStatement("select id from transactions where trans_id=? and provider=? and amount=? and cust_id is null");
        st.setLong(1, trans_id);
        st.setString(2, provider);
        st.setInt(3, amount);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            System.out.println("Paying...");
            //  UPDATE subscription SET status = true WHERE id in (", ");
            PreparedStatement st2 = client.connection.prepareStatement("UPDATE transactions SET cust_id = ? WHERE trans_id = ?");
            st2.setInt(1, this.id);
            st2.setLong(2, trans_id);
            st2.execute();
            
            for (Integer sub : subscriptionIDs) {
                PreparedStatement st3 = client.connection.prepareStatement("UPDATE subscription SET status = true WHERE id = ?");
                st3.setInt(1, sub);
                st3.execute();
            }
            
            
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "No transactions found!", "Transaction error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
