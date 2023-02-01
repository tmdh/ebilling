
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
    String phone;
    String address;
    String password;
    int id;
    
    public User(int id) {
        this.id = id;
        DatabaseClient client = new DatabaseClient();
        try {
            PreparedStatement st = client.connection.prepareStatement("select name, email, phone, address, password from user where id=?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            this.name = rs.getString("name");
            this.email = rs.getString("email");
            this.phone = rs.getString("phone");
            this.address = rs.getString("address");
            this.password = rs.getString("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }
    
    public static User create(String email, String password, String confirmPassword, String name) throws Exception {
        DatabaseClient client = new DatabaseClient();
        if (!password.equals(confirmPassword)) {
            client.close();
            throw new Exception("Password fields do not match.");
        }
        PreparedStatement st = client.connection.prepareStatement("insert into user(name,email,password) values( ? , ? , ? )");
        st.setString(1, name);
        st.setString(2, email);
        st.setString(3, password);
        st.execute();
        PreparedStatement st2 = client.connection.prepareStatement("select id from user where email = ?");
        st2.setString(1, email);
        ResultSet rs = st2.executeQuery();
        int id2 = rs.getInt("id");
        client.close();
        return new User(id2);
    }
    
    public static User find(String email, String password) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select id from user where email=? and password=?");
        st.setString(1, email);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            int id2 = rs.getInt("id");
            client.close();
            return new User(id2);
        } else {
            client.close();
            throw new Exception("Email and password do not match");
        }
    }
    
    public void updateInfo(String name, String email, String phone, String address) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("UPDATE user set name = ?, email = ?, phone = ?, address = ? WHERE id = ?");
        st.setString(1, name);
        st.setString(2, email);
        st.setString(3, phone);
        st.setString(4, address);
        st.setInt(5, id);
        st.execute();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        client.close();
    }
    
    public void updatePassword(String oldPassword, String newPassword, String confirmNewPassword) throws Exception {
        if (!oldPassword.equals(this.password)) {
            throw new Exception("The current password does not match");
        } else if (!newPassword.equals(confirmNewPassword))  {
            throw new Exception("The new password does not match with retyped password");
        } else {
            DatabaseClient client = new DatabaseClient();
            PreparedStatement st = client.connection.prepareStatement("UPDATE user set password = ? WHERE id = ?");
            st.setString(1, newPassword);
            st.setInt(2, id);
            st.execute();
            this.password = newPassword;
            client.close();
        }
    }
    
    public int countSubscriptions() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select count(id) from subscription where cust_id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        int count = rs.getInt(1);
        client.close();
        return count;
    }
    
    public int totalBillPerMonth() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select sum(package.price) from subscription inner join package on package.id=subscription.pkg_id and subscription.cust_id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        int sum = rs.getInt(1);
        client.close();
        return sum;
    }
    
    public Vector<Vector<Object>> subscriptions() throws Exception {
        DatabaseClient client = new DatabaseClient();
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
        client.close();
        return v;
    }
    
    public boolean pay(long trans_id, String provider, int amount, Integer[] subscriptionIDs) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select id from transactions where trans_id=? and provider=? and amount=? and cust_id is null");
        st.setLong(1, trans_id);
        st.setString(2, provider);
        st.setInt(3, amount);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            PreparedStatement st2 = client.connection.prepareStatement("UPDATE transactions SET cust_id = ? WHERE trans_id = ?");
            st2.setInt(1, this.id);
            st2.setLong(2, trans_id);
            st2.execute();
            for (Integer sub : subscriptionIDs) {
                PreparedStatement st3 = client.connection.prepareStatement("UPDATE subscription SET status = true WHERE id = ?");
                st3.setInt(1, sub);
                st3.execute();
            }
            client.close();
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "No transactions found!", "Transaction error", JOptionPane.ERROR_MESSAGE);
        }
        client.close();
        return false;
    }
    
    public void insertSubscription(int pkg_id) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("insert into subscription (cust_id, pkg_id) values (?, ?)");
        st.setInt(1, id);
        st.setInt(2, pkg_id);
        st.execute();
        client.close();
    }
}
