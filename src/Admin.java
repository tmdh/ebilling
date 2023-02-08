import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

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
    
    public static Vector<Vector<Object>> users() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select id, name, email, phone, address from user");
        ResultSet rs = st.executeQuery();
        Vector<Vector<Object>> v = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> a = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= 5; columnIndex++) {
                a.add(rs.getObject(columnIndex));
            }
            v.add(a);
        }
        client.close();
        return v;
    }
    
    public static Vector<Vector<Object>> complains() throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select complains.id, user.name, title from complains, subscription, user where complains.id=subscription.id and subscription.cust_id=user.id");
        ResultSet rs = st.executeQuery();
        Vector<Vector<Object>> v = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> a = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= 3; columnIndex++) {
                a.add(rs.getObject(columnIndex));
            }
            v.add(a);
        }
        client.close();
        return v;
    }
    
    public static Vector<Object> complain(int id) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("select user.name, package.name, title, body from complains, subscription, user, package\n" +
        "where complains.id=subscription.id and subscription.cust_id=user.id and subscription.pkg_id=package.id and complains.id=?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        rs.next();
        Vector<Object> v = new Vector<>();
        v.add(rs.getString(1));
        v.add(rs.getString(2));
        v.add(rs.getString(3));
        v.add(rs.getString(4));
        client.close();
        return v;
    }
    
    public static void reply(int id) throws Exception {
        DatabaseClient client = new DatabaseClient();
        PreparedStatement st = client.connection.prepareStatement("DELETE FROM complains WHERE id=?");
        st.setInt(1, id);
        st.execute();
        client.close();
    }
    
}
