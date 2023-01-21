/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */

import java.sql.*;

public class DatabaseClient {
    Connection connection = null;
        
    public DatabaseClient() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:ebilling.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
