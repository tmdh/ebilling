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
public class Admin {
    public static int countSubscriptions() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select count(id) from subscription");
        ResultSet rs = st.executeQuery();
        int count = rs.getInt(1);
        client.close();
        return count;
    }
    
    public static int countComplains() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select count(id) from complains");
        ResultSet rs = st.executeQuery();
        int count = rs.getInt(1);
        client.close();
        return count;
    }
    
    public static int countUsers() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select count(id) from user");
        ResultSet rs = st.executeQuery();
        int count = rs.getInt(1);
        client.close();
        return count;
    }
    
    public static int monthlyTotalIncome() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select sum(price) from package, subscription where package.id=subscription.pkg_id");
        ResultSet rs = st.executeQuery();
        int count = rs.getInt(1);
        client.close();
        return count;
    }
    
    public static Vector<Vector<Object>> subscriptions() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select subscription.cust_id, subscription.id, package.name, package.price, subscription.billing_day, package.bandwidth, subscription.status from subscription inner join package on package.id=subscription.pkg_id");
        ResultSet rs = st.executeQuery();
        Vector<Vector<Object>> v = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> a = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= 7; columnIndex++) {
                if (columnIndex == 7) {
                    a.add(new Boolean(rs.getBoolean(7)));
                } else {
                    a.add(rs.getObject(columnIndex));
                }
            }
            v.add(a);
        }
        client.close();
        return v;
    }
    
    public static Vector<Vector<Object>> users() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select id, name, email, phone, address from user");
        ResultSet rs = st.executeQuery();
        Vector<Vector<Object>> v = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> a = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= 5; columnIndex++) {/*
                if (columnIndex == 7) {
                    a.add(new Boolean(rs.getBoolean(7)));
                } else {*/
                    a.add(rs.getObject(columnIndex));
                //}
            }
            v.add(a);
        }
        client.close();
        return v;
    }
    
}
