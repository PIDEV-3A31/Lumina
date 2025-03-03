package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class interfaceHome {

    @FXML
    private Pane button_municipality;

    @FXML
    private Pane button_transport;

    @FXML
    private Pane button_traffic;



    @FXML
    public void initialize() {
        // Action associée au pane "pane"
        button_transport.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/interface_user_transport.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button_municipality.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/fxml/documents_user.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button_traffic.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/gestion_traffic.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleRedirection(MouseEvent event, String fxmlPath) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        // Récupérer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Changer la scène
        stage.setScene(scene);
        stage.show();
    }

}