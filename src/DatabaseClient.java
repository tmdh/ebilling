/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tareque
 */
import java.sql.*;
import java.io.File;

public class DatabaseClient {

    private static final String DB_FOLDER = getOSAppDataFolder() + File.separator + "ebilling";
    private static final String DB_PATH = DB_FOLDER + File.separator + "ebilling.db";
    Connection connection = null;

    public DatabaseClient() {
        this.connection = getConnection();
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            createDatabase();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        return connection;
    }

    private static String getOSAppDataFolder() {
        String appDataFolder = System.getProperty("user.home");

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            appDataFolder = System.getenv("APPDATA");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            appDataFolder = System.getProperty("user.home") + File.separator + ".config";
        }

        return appDataFolder;
    }

    public void createDatabase() {
        try {
            File folder = new File(DB_FOLDER);
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                    System.out.println("Failed to create the database folder.");
                    return;
                }
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);

            if (connection != null) {
                Statement statement = connection.createStatement();

                String sqlDump = "PRAGMA foreign_keys=OFF;\n"
                        + "BEGIN TRANSACTION;\n"
                        + "CREATE TABLE package (id INTEGER PRIMARY KEY ASC AUTOINCREMENT UNIQUE NOT NULL, name VARCHAR (30), bandwidth INTEGER, price INTEGER, description VARCHAR (150));\n"
                        + "INSERT INTO package VALUES(1,'Personal',3,100,'This package is suitable for basic browsing of social networks and communication apps.');\n"
                        + "INSERT INTO package VALUES(2,'Home',10,200,'This package is suitable for moderate browsing for a family and streaming online TVs.');\n"
                        + "INSERT INTO package VALUES(3,'Gaming',20,300,'This package is suitable for gaming in low latency high speed internet.');\n"
                        + "INSERT INTO package VALUES(4,'Business',100,700,'This package is suitable for large business which is able to handle 20-30 employees.');\n"
                        + "INSERT INTO package VALUES(5,'Netflix',5,150,'Netflix subscription for a single user.');\n"
                        + "CREATE TABLE payments (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, trans_id REFERENCES transactions (id), sub_id REFERENCES subscription (id), date DATETIME NOT NULL AS (datetime()));\n"
                        + "CREATE TABLE transactions (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, trans_id INTEGER NOT NULL UNIQUE, provider VARCHAR (10) NOT NULL, amount INTEGER NOT NULL, cust_id REFERENCES user (id));\n"
                        + "INSERT INTO transactions VALUES(1,123,'bKash',1000,NULL);\n"
                        + "INSERT INTO transactions VALUES(2,456,'bKash',400,NULL);\n"
                        + "CREATE TABLE subscription (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, cust_id INTEGER REFERENCES user (id) NOT NULL, pkg_id INTEGER REFERENCES package (id) NOT NULL, billing_day INTEGER DEFAULT (strftime('%d')), status BOOLEAN DEFAULT (false));\n"
                        + "INSERT INTO subscription VALUES(1,30,2,8,0);\n"
                        + "INSERT INTO subscription VALUES(2,31,1,8,0);\n"
                        + "INSERT INTO subscription VALUES(3,17,1,8,0);\n"
                        + "INSERT INTO subscription VALUES(4,18,4,5,0);\n"
                        + "INSERT INTO subscription VALUES(5,19,3,5,0);\n"
                        + "INSERT INTO subscription VALUES(6,20,3,21,0);\n"
                        + "INSERT INTO subscription VALUES(7,17,3,21,0);\n"
                        + "INSERT INTO subscription VALUES(8,18,4,1,0);\n"
                        + "INSERT INTO subscription VALUES(9,1,1,1,0);\n"
                        + "INSERT INTO subscription VALUES(10,3,1,2,0);\n"
                        + "INSERT INTO subscription VALUES(11,1,3,2,0);\n"
                        + "INSERT INTO subscription VALUES(12,35,1,5,0);\n"
                        + "INSERT INTO subscription VALUES(13,35,3,5,0);\n"
                        + "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, email VARCHAR (50) UNIQUE, name VARCHAR (50), password VARCHAR (50), phone VARCHAR (15), address VARCHAR (255));\n"
                        + "INSERT INTO user VALUES(1,'a','Mesbah','123','01313131313','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(17,'shahadat@gmail.com','Shahadat','l','01221221212','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(18,'tushar@gmail.com','Tushar','m','01251158268','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(19,'safiul@gmail.com','Safiul','i','01287854554','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(20,'alif@gmail.com','Alif','il','01712268412','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(21,'hadi@gmail.com','Hadi','test','01786262493','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(22,'sefat@gmail.com','Sefat','lp','01815266633','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(23,'aimon@gmail.com','Aimon','k','01922255222','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(24,'ikram@gmail.com','Ikram','g','01741111452','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(25,'shohan@gmail.com','Shohan','r','01222222222','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(26,'shawon@gmail.com','Shawon','www','01888888888','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(27,'alifsh@gmail.com','Alif Sheikh','pass','01777777777','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(28,'paran@gmail.com','Paran','wwww','01555555555','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(29,'anik@gmail.com','Anik','tt','01888888844','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(30,'tmdh@gmail.com','Tareque','ty','01322656523','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(31,'sajid@gmail.com','Sajid','pt','01349761641','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(32,'sakkib@gmail.com','Sakib','pass','01547974612','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(33,'test@test.com','Test user','p','01691312462','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(34,'reza@gmail.com','Reza','ttt','01897461231','CUET, Pahartali, Ctg.');\n"
                        + "INSERT INTO user VALUES(35,'tarequemd.hanif@yahoo.com','Tareque Md Hanif','password','01257943165','CUET, Pahartali, Ctg.');\n"
                        + "CREATE TABLE complains (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sub_id REFERENCES subscription (id), body VARCHAR (500), title VARCHAR (100));\n"
                        + "INSERT INTO complains VALUES(4,12,'vai net thik koren',NULL);\n"
                        + "INSERT INTO complains VALUES(5,13,'not working',NULL);\n"
                        + "DELETE FROM sqlite_sequence;\n"
                        + "INSERT INTO sqlite_sequence VALUES('package',5);\n"
                        + "INSERT INTO sqlite_sequence VALUES('transactions',2);\n"
                        + "INSERT INTO sqlite_sequence VALUES('subscription',13);\n"
                        + "INSERT INTO sqlite_sequence VALUES('user',35);\n"
                        + "INSERT INTO sqlite_sequence VALUES('complains',5);\n"
                        + "COMMIT;";

                String[] statements = sqlDump.split(";");
                for (String statementString : statements) {
                    statement.execute(statementString);
                }

                System.out.println("Database created successfully.");
                connection.close();
            } else {
                System.out.println("Failed to create the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
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
