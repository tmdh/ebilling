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
    
    
    
    
}
