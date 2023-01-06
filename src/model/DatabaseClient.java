package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */

import java.sql.*;
import java.util.logging.*;

public class DatabaseClient {
    public Connection connection = null;
    
    private Logger logger = Logger.getLogger("DatabaseClient");
    
    public DatabaseClient() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:ebilling.db");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
