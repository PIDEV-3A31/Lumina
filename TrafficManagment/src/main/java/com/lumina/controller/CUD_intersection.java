package com.lumina.controller;

import com.lumina.models.Intersection;
import com.lumina.services.ServiceIntersection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class CUD_intersection {
    public TextField intersection_name_add;
    public TextField intersection_long_add;
    public TextField intersection_lat_add;
    public TextField Intersection_id;
    private Stage addIntersectionStage;
   

    // intersection_name_add




    public void setStage(Stage stage) {
        this.addIntersectionStage = stage;
    }

    @FXML
    public void CloseWindow() {
        if (addIntersectionStage != null) {
            addIntersectionStage.close(); // Close the current window
        }
    }


    @FXML
    private void handleHyperlinkClick() {
        try {
            // Specify the URL you want to open
            String url = "https://www.google.com/maps/@36.8510414,10.159122,15z?entry=ttu&g_ep=EgoyMDI1MDIxMi4wIKXMDSoASAFQAw%3D%3D";

            // Use Desktop API to open the URL in the default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("Desktop is not supported, cannot open URL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void AddIntersection(ActionEvent actionEvent) {
        ServiceIntersection serviceIntersection = new ServiceIntersection();

        String intersectionName = intersection_name_add.getText();
        String longitudeText = intersection_long_add.getText(); // Corrected: Correct variable for longitude
        String latitudeText = intersection_lat_add.getText();   // Correct variable for latitude

        if (intersectionName.isEmpty() || longitudeText.isEmpty() || latitudeText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Fields can't be empty.");
            alert.show();
            return;  // Early exit if any field is empty
        }

        // Initialize Float values for longitude and latitude
        Float longitude = 0.0f;
        Float latitude = 0.0f;

        try {
            // Convert the text values into float
            longitude = Float.parseFloat(longitudeText);
            latitude = Float.parseFloat(latitudeText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Longitude and Latitude must be valid numbers.");
            alert.show();
            return; // Early exit if parsing fails
        }

        // Create a new Intersection object with the parsed values
        Intersection intersection = new Intersection(intersectionName, longitude, latitude, 10, 1); // Use 10 and 1 as placeholders for other values

        // Add the intersection using the service
        serviceIntersection.addIntersection(intersection);

        // Show a success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Intersection added successfully.");
        alert.show();
    }

    public void RemoveIntersection(ActionEvent actionEvent) {
        ServiceIntersection serviceIntersection = new ServiceIntersection();
        String intersectionID = Intersection_id.getText();

        // Check if the intersection ID is empty
        if (intersectionID.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please provide an intersection ID.");
            alert.show();
            return;
        }

        // Try to parse the intersection ID as an integer
        try {
            int id = Integer.parseInt(intersectionID); // Assuming ID is an integer
            Intersection temp = new Intersection();
            temp.setId(id);

            // Remove the intersection using the service
            serviceIntersection.removeIntersection(temp);

            // Show a success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Intersection removed successfully.");
            alert.show();
        } catch (NumberFormatException e) {
            // Handle the case where the ID is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Intersection ID. Please enter a valid number.");
            alert.show();
        }
    }
    public void ModifyIntersection(ActionEvent actionEvent) {
        ServiceIntersection serviceIntersection = new ServiceIntersection();
        String intersectionID = Intersection_id.getText();

        // Check if the intersection ID is empty
        if (intersectionID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please provide an intersection ID.");
            return;
        }

        try {
            int id = Integer.parseInt(intersectionID); // Assuming ID is an integer

            Intersection existingIntersection = serviceIntersection.getIntersectionById(id);

            if (existingIntersection != null) {
                String newName = intersection_name_add.getText();
                String longitudeText = intersection_long_add.getText();
                String latitudeText = intersection_lat_add.getText();

                boolean updated = false;  // To track if any modification is made

                // Modify the name only if it is provided and different from the existing one
                if (!newName.isEmpty() && (existingIntersection.getName() == null || !existingIntersection.getName().equals(newName))) {
                    existingIntersection.setName(newName);
                    updated = true;
                }

                // Modify longitude only if it is provided
                if (!longitudeText.isEmpty()) {
                    existingIntersection.setLongitude(Float.parseFloat(longitudeText));
                    updated = true;
                }

                // Modify latitude only if it is provided
                if (!latitudeText.isEmpty()) {
                    existingIntersection.setLatitude(Float.parseFloat(latitudeText));
                    updated = true;
                }

                // If any modification was made, update the intersection
                if (updated) {
                    serviceIntersection.updateIntersection(existingIntersection);
                    showAlert(Alert.AlertType.INFORMATION, "Information", "Intersection modified successfully.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Information", "No modifications were made.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Intersection not found.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please enter valid numbers for ID, longitude, and latitude.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
    }






