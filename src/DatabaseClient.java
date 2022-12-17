/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */

import java.sql.*;
import javax.swing.JOptionPane;
import java.util.Vector;


public class DatabaseClient {
    Connection connection = null;
    Statement statement = null;
    
    public DatabaseClient() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:ebilling.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error in connecting to database", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error in connecting to database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public User createUser(String email, String password, String confirmPassword, String name) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("Password fields do not match.");
        }
        
        // Create User in the database
        PreparedStatement st = connection.prepareStatement("insert into user (email, name, password) values (?, ?, ?)");
        st.setString(1, email);
        st.setString(2, name);
        st.setString(3, password);
        if (st.execute()) {
            st = connection.prepareStatement("select id from user where email=?");
            st.setString(1, email);
            st.executeQuery();
            ResultSet rs = st.executeQuery();
            return new User(rs.getInt("id"));
        }
        return null;
    }
    
}
