package com.esprit.controllers;

import com.esprit.models.Intersection;
import com.esprit.services.ServiceIntersection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.web.WebView;

import java.io.IOException;
public class DisplayIntersections {

    public Label traffic_density;
    public Label intersection_title;
    @FXML
    public WebView map;
    public Button getCords;
    @FXML
    private TableView<Intersection> intersection_table;

    @FXML
    private TableColumn<Intersection, Integer> intersection_id;

    @FXML
    private TableColumn<Intersection, String> intersection_name;

    @FXML
    private TableColumn<Intersection, Float> intersection_density;

    // This method is automatically called when the FXML is loaded
    @FXML
    public void initialize() {
        ShowData();
        // In the initialize method or elsewhere
        intersection_table.setOnMouseClicked(event -> ShowIntersectionONMaps());
        // Load the map in the WebView
        String GoogleMapsEmbedding = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d15633.84701723704!2d10.169757788201208!3d36.850617327160805!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e0!3m2!1sen!2stn!4v1739792162611!5m2!1sen!2stn\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        String Google = "https://www.google.com/maps/@36.8486419,10.1721106,13z/data=!5m1!1e1?entry=ttu&g_ep=EgoyMDI1MDIxMi4wIKXMDSoASAFQAw%3D%3D";
        WebEngine webEngine = map.getEngine();
        webEngine.load(Google);
    }


    public void ShowIntersectionONMaps() {
        // Get the selected intersection item
        Intersection selectedIntersection = intersection_table.getSelectionModel().getSelectedItem();

        if (selectedIntersection != null) {
            // Get the latitude and longitude of the selected intersection
            double latitude = selectedIntersection.getLatitude();
            double longitude = selectedIntersection.getLongitude();

            // Construct the Google Maps URL with the selected coordinates
            String url = String.format("https://www.google.com/maps/@%f,%f,10.51z", latitude, longitude);
            System.out.println(url);
            // Get the WebEngine from the WebView
            WebEngine webEngine = map.getEngine();

            // Load the new URL in the WebView to display the updated map
            webEngine.load(url);
        } else {
            // If no intersection is selected, show a warning or handle as needed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Intersection Selected");
            alert.setContentText("Please select an intersection from the table.");
            alert.showAndWait();
        }
    }


    // This method loads the data into the TableView
    public void ShowData() {
        ServiceIntersection SIC = new ServiceIntersection();

        // Get data from the service (assuming it returns a list of Intersection objects)
        ObservableList<Intersection> intersectionList = FXCollections.observableList(SIC.getAllIntersection());

        intersection_table.setItems(intersectionList);
        intersection_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        intersection_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        intersection_density.setCellValueFactory(new PropertyValueFactory<>("traffic_density"));
    }

    public void ShowIntersectionsV2() {
        ServiceIntersection SIC = new ServiceIntersection();
        for (Intersection intersection : SIC.getAllIntersection()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DisplayIntersections.class.getResource("/cardItem.fxml"));
            // Add more logic to continue adding card items here.
        }
    }

    public void ShowAddIntersection() {
        try {
            // Get coordinates from the map
            double[] cords = GetTheCordsOfTheIntersectionFromMap();

            // Check if the coordinates are invalid (both are 0)
            if (cords == null || cords.length != 2 || cords[0] == 0 || cords[1] == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Coordinates");
                alert.setHeaderText("Coordinates Not Chosen");
                alert.setContentText("You need to choose coordinates on the map!");
                alert.showAndWait();
                return;
            }

            // Load the FXML file for the Add Intersection screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_ui.fxml"));
            VBox root = loader.load();  // Load the root element from the FXML

            // Get the controller and set the coordinates
            CUD_intersection cud_intersection = loader.getController();
            cud_intersection.setCords(cords);

            // Create a new stage for the Add Intersection window
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal
            addIntersectionStage.setTitle("Add Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();

            // Optionally, wait for the modal to close before continuing with ShowData() (if needed)
            addIntersectionStage.setOnHidden(e -> ShowData());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void TrafficLightCrud() {
        try {
            // Load the child FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/trafficLightManagement.fxml"));
            AnchorPane root = loader.load();
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("Traffic Light Management");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowData();
    }

    public void ShowDeleteIntersection() {
        try {
            // Load the child FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/delete_ui.fxml"));
            VBox root = loader.load();
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("Delete Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowData();
    }

    public void ShowModifyIntersection() {
        try {
            // Load the child FXML
            System.out.println("Modify UI Loaded");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modify_ui.fxml"));
            VBox root = loader.load();
            // Create a new Stage for the child window
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("Modify Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowData();
    }
    public double[] GetTheCordsOfTheIntersectionFromMap() {
        WebEngine webEngine = map.getEngine();

        String jsScript = "var element = document.querySelector('.ZqLNQd.t9f27'); element ? (element.textContent || element.innerText) : 'Element not found';";

        Object result = webEngine.executeScript(jsScript);

        if (result instanceof String content) {
            // Try parsing the content into coordinates (assuming format "x, y")
            content = content.trim();  // Trim any leading/trailing spaces
            if (!content.isEmpty()) {  // Check if the content is not empty
                String[] coords = content.split(",");
                if (coords.length == 2) {
                    try {
                        // Parse the x and y coordinates, trimming any extra spaces
                        double x = Double.parseDouble(coords[0].trim());
                        double y = Double.parseDouble(coords[1].trim());
                        System.out.println("Coordinates: " + x + ", " + y);
                        return new double[]{x, y};  // Return the coordinates as an array
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid coordinate format. Coordinates should be integers.");
                    }
                } else {
                    System.out.println("Error: Coordinate format is incorrect. Expected format: 'x, y'.");
                }
            } else {
                System.out.println("Error: Coordinates content is empty.");
            }
        } else {
            System.out.println("Error: Unable to retrieve content. Result is not a string.");
        }

// Return default coordinates if content is not found or there was an error
        return new double[]{0, 0};

    }



}
