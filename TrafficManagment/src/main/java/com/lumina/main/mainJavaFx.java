package com.lumina.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class mainJavaFx extends Application {

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set it as the root of the scene
        AnchorPane root = FXMLLoader.load(getClass().getResource("/interface_commune.fxml"));

        // Set up the scene and stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Set the window title and show it
        primaryStage.setTitle("JavaFX Application");
        primaryStage.show();
    }
}
