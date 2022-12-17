
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    
    static DatabaseClient client = new DatabaseClient();
    
    public User(int id) {
        this.id = id;
        try {
            PreparedStatement st = client.connection.prepareStatement("select name, email from user where id=?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            this.name = rs.getString("name");
            this.email = rs.getString("email");
        } catch (Exception e) {}
    }
    
    public static User create(String email, String password, String confirmPassword, String name) throws Exception {
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
        return new User(rs.getInt("id"));
    }
}
