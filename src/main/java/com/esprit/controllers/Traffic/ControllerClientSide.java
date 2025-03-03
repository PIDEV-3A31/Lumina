package com.esprit.controllers.Traffic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerClientSide {

    // Method to route to User Home page
    public void routeToUserHome() {
        loadPage("/UserGestionTraffic.fxml", "User Home");
    }

    // Method to route to Admin Home page
    public void routeToAdminHome() {
        loadPage("/gestion_traffic.fxml", "Admin Home");
    }

    public void routeToTrafficInterface() {
        loadPage("/interface_home.fxml", "Client Traffic");
    }


    // Helper method to load the FXML file and display the new window
    private void loadPage(String fxmlPath, String title) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            AnchorPane root = loader.load();  // Correct root element class (AnchorPane)

            Stage newStage = new Stage();
            newStage.setTitle(title);  // Set a new title for the window

            newStage.setScene(new Scene(root));
            newStage.show();  // Display the new window
        } catch (Exception e) {
            e.printStackTrace();  // Handle the exception, for example logging or showing an error message
        }
    }
}
