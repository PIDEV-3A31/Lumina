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
        String GoogleMapsEmbedding = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d15633.84701723704!2d10.169757788201208!3d36.850617327160805!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e0!3m2!1sen!2stn!4v1739792162611!5m2!1sen!2stn\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        ShowData();



    }

    public void ShowData(){
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
        ShowData();
    }

    public void ShowDeleteIntersection(ActionEvent actionEvent) {
        try {
            // Load the child FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/delete_ui.fxml"));
            VBox root = loader.load();
            Stage addIntersectionStage = new Stage();
            addIntersectionStage.initModality(Modality.APPLICATION_MODAL); // Make it modal (on top of the main window)
            addIntersectionStage.setTitle("delete Intersection");
            addIntersectionStage.setScene(new Scene(root));
            addIntersectionStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowData();
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
        ShowData();
    }
}
