
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */
public class Package {
    int id;
    String name;
    String description;
    int price;
    int bandwidth;
    
    static DatabaseClient client = new DatabaseClient();

    public Package(int id, String name, String description, int price, int bandwidth) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bandwidth = bandwidth;
    }
    
    public static Vector<Package> all() throws SQLException {
        ResultSet rs = client.statement.executeQuery("select * from package");
        Vector<Package> v = new Vector<>();
        while (rs.next()) {
            v.add(new Package(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("price"), rs.getInt("bandwidth")));
        }
        return v;
    }
    
}
