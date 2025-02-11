package come.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;
    private static DataBase instance;
    private final String URL = "jdbc:mysql://localhost:3306/pidev";
    private String user ="root";
    private String password ="";
    private Connection cnx;


    public DataBase() {
        try {
            connection = DriverManager.getConnection(URL,user,password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase(); //creer une instance si il  n'y aucune instance
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
