package com.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private Connection connection;
    private static DataBase instance;
    private final String URL = "jdbc:mysql://localhost:3307/nour3";
    private final String USER = "root";
    private final String PASSWORD = "";

    private DataBase() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/nour3", "root", "");
            System.out.println("Connected to database");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.out.println(e.getMessage());
        }

    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
