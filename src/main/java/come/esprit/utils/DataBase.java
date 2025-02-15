package come.esprit.utils;

import come.esprit.models.Parking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

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
