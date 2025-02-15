package com.esprit.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.Objects;

public class Home extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        /*try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUser.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }*/

        try {
            // Charge le fichier FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/loginn.fxml"))); // Assure-toi que le chemin est correct
            // Crée la scène avec le FXML chargé
            Scene scene = new Scene(root);
            // Configure la fenêtre
            stage.setTitle("Login Page");
            stage.setScene(scene);
            scene.getStylesheets().add("style.css");
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
