package com.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;
    private static DataBase instance;

    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static DataBase getInstance() {
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }

    public DataBase() {
        try{
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connection established");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection(){
        return connection;
    }


}