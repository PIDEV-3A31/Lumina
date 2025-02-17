package com.esprit.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class AdminUser {

    @FXML
    private Button admin_home;

    @FXML
    private Button user_home;

    @FXML
    private void initialize() {
        admin_home.setOnAction(event -> loadScene(event, "/fxml/afficherDocuments.fxml"));
        user_home.setOnAction(event -> loadScene(event, "/fxml/interfaceHome.fxml"));
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
