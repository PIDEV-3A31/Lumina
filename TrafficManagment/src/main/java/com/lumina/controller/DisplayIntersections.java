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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplayIntersections {

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
        // Create service object to get intersections
        ServiceIntersection SIC = new ServiceIntersection();

        // Get data from the service (assuming it returns a list of Intersection objects)
        ObservableList<Intersection> intersectionList = FXCollections.observableList(SIC.getAllIntersection());
        intersectionList.forEach(intersection -> System.out.println(intersection));

        intersection_table.setItems(intersectionList);
        intersection_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        intersection_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        intersection_density.setCellValueFactory(new PropertyValueFactory<>("traffic_density"));



    }

    public void ShowAddIntersection(javafx.event.ActionEvent actionEvent) {
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
    }

    public void ShowDeleteIntersection(ActionEvent actionEvent) {
        try {
            // Load the child FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/delete_ui.fxml"));
            VBox root = loader.load();

            // Create a new Stage for the child window
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("delete Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowModifyIntersection(ActionEvent actionEvent) {
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
    }
}
