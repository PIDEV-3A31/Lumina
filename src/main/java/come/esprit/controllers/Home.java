package come.esprit.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterParking"));
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
