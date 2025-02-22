package com.lumina.controller;

import com.lumina.models.Intersection;
import com.lumina.services.ServiceIntersection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class DisplayIntersections {

    public Label traffic_density;
    public Label intersection_title;
    @FXML
    public WebView map;
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

        // Load the map in the WebView
        String GoogleMapsEmbedding = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d15633.84701723704!2d10.169757788201208!3d36.850617327160805!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e0!3m2!1sen!2stn!4v1739792162611!5m2!1sen!2stn\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        String Google = "https://www.google.com/maps/@36.8486419,10.1721106,13z/data=!5m1!1e1?entry=ttu&g_ep=EgoyMDI1MDIxMi4wIKXMDSoASAFQAw%3D%3D";
        WebEngine webEngine = map.getEngine();
        webEngine.load(Google);

        // Set hover effect on the TableView
        addTableHoverEffect();
    }

    // This method sets up the hover effect on the TableView
    private void addTableHoverEffect() {
        // Mouse entered event for hover effect
        intersection_table.setOnMouseEntered((MouseEvent event) -> {
            intersection_table.setStyle("-fx-background-color: #e0f7f7; -fx-border-color: #0CBEB8; -fx-border-width: 1px;");
        });

        // Mouse exited event to revert the hover effect
        intersection_table.setOnMouseExited((MouseEvent event) -> {
            intersection_table.setStyle("-fx-background-color: white; -fx-border-color: #0CBEB8; -fx-border-width: 1px;");
        });
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
            // Load the child FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_ui.fxml"));
            VBox root = loader.load();

            // Create a new Stage for the child window
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("Add Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowData();
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
}
