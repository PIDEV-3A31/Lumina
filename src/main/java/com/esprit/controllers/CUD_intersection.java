package com.esprit.controllers;

import com.esprit.models.Intersection;
import com.esprit.services.ServiceIntersection;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class CUD_intersection implements Initializable {
    @FXML
    public Button deleteBtn;

    public void setCords(double[] cords) {
        this.cords = cords;
    }

    private double[] cords;
    @FXML
    public TextField intersection_name_add;
    @FXML
    public TextField intersection_long_add;
    @FXML
    public TextField intersection_lat_add;
    public TextField Intersection_id;
    private Stage addIntersectionStage;

    @FXML
    private void handleHyperlinkClick()  {
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

        // Check if cords is null or not set
        if (cords == null || cords.length != 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Coordinates are not properly set.");
            alert.show();
            return;
        }

        String intersectionName = intersection_name_add.getText();
        String longitudeText = String.valueOf(cords[0]);
        String latitudeText = String.valueOf(cords[1]);

        if (intersectionName.isEmpty() || longitudeText.isEmpty() || latitudeText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Fields can't be empty.");
            alert.show();
            return;  // Early exit if any field is empty
        }

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

        Intersection intersection = new Intersection(intersectionName, latitude, longitude, 10, 1); // Use 10 and 1 as placeholders for other values

        serviceIntersection.addIntersection(intersection);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Intersection added successfully.");
        alert.show();
    }

    public void RemoveIntersection(ActionEvent actionEvent) throws InterruptedException {
        ServiceIntersection serviceIntersection = new ServiceIntersection();
        String intersectionID = Intersection_id.getText();

        // Create a task to run the FaceIDVerificationCheck
        Task<Boolean> faceIDTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    // Run FaceID verification in the background
                    while (!HelperClass.FaceIDVerificationCheck()) {
                        // Periodically check for verification
                        Thread.sleep(500);
                    }
                    return true; // Return true when verification succeeds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };

        // Start the Task in a background thread
        new Thread(faceIDTask).start();

        // Update UI on task success
        faceIDTask.setOnSucceeded(event -> {
            // When FaceID verification is done, update the button state
            Platform.runLater(() -> {
                deleteBtn.setDisable(false);  // Enable the delete button
            });
        });

        // UI update on failure
        faceIDTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                deleteBtn.setDisable(true);  // Keep the delete button disabled
            });
        });

        // Keep UI responsive while waiting for the result
        faceIDTask.setOnRunning(event -> {
            // This can be used to show a progress indicator or change the button text
            Platform.runLater(() -> {
                deleteBtn.setText("Verifying..."); // Update button text to show ongoing verification
            });
        });

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
            deleteBtn.setDisable(true);

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

                if (!newName.isEmpty() && (existingIntersection.getName() == null || !existingIntersection.getName().equals(newName))) {
                    existingIntersection.setName(newName);
                    updated = true;
                }

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Make sure deleteBtn is not null before interacting with it
        if (deleteBtn != null) {
            deleteBtn.setDisable(true);

            // Create a task to run the FaceIDVerificationCheck and update the UI in real-time
            Task<Boolean> faceIDTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    try {
                        while (!HelperClass.FaceIDVerificationCheck()) {
                            Thread.sleep(500);  // Poll every 500ms
                        }
                        return true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            };

            // Start the Task in a background thread
            new Thread(faceIDTask).start();

            // UI updates when task succeeds or fails
            faceIDTask.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    try {
                        if (deleteBtn != null) {
                            deleteBtn.setDisable(false); // Enable button after FaceID check passes
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });

            faceIDTask.setOnFailed(event -> {
                Platform.runLater(() -> {
                    try {
                        if (deleteBtn != null) {
                            deleteBtn.setDisable(true); // Keep the button disabled if FaceID fails
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });


            faceIDTask.setOnRunning(event -> {
                Platform.runLater(() -> {
                    if (deleteBtn != null) {
                        deleteBtn.setText("Verifying..."); // Update button text in real-time
                    }
                });
            });
        } else {
            System.out.println("deleteBtn is null, skipping initialization.");
        }
    }


}
