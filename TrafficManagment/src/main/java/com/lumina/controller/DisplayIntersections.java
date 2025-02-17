package com.lumina.controller;

import com.lumina.models.Intersection;
import com.lumina.services.ServiceIntersection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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

}
