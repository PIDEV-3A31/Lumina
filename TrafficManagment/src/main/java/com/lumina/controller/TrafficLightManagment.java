package com.lumina.controller;

import com.lumina.models.TrafficLight;
import com.lumina.services.ServiceTrafficLight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TrafficLightManagment implements Initializable {

    // Define TableColumns with the correct types and bind them to the respective fields
    @FXML
    private TableColumn<TrafficLight, Integer> inter_id;
    @FXML
    private TableColumn<TrafficLight, String> name;
    @FXML
    private TableColumn<TrafficLight, Integer> state;
    @FXML
    private TableColumn<TrafficLight, Integer> nbr_car;
    @FXML
    private TableColumn<TrafficLight, Integer> waittime;

    @FXML
    private TableView<TrafficLight> traffic_table;

    @FXML
    public TextField traffic_id;
    @FXML
    public TextField traffic_name;
    @FXML
    public TextField traffic_intersection_id;
    @FXML
    public TextField traffics_state;
    @FXML
    public TextField traffics_waittime;

    // Initialize the table with data from the service
    void showdata() {
        ServiceTrafficLight STL = new ServiceTrafficLight();
        ObservableList<TrafficLight> data = FXCollections.observableList(STL.getAllTrafficLight());

        traffic_table.setItems(data);

        // Make sure to correctly reference the columns for each field
        inter_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        waittime.setCellValueFactory(new PropertyValueFactory<>("CurrentWaitTime"));
    }

    @FXML
    public void addTrafficLight() {
        try {
            // Input validation
            if (traffic_name.getText().isEmpty() ||
                    traffic_intersection_id.getText().isEmpty() || traffics_state.getText().isEmpty() ||
                    traffics_waittime.getText().isEmpty()) {

                showAlert("Input Error", "Please fill in all fields", AlertType.ERROR);
                return;
            }

            // Parse input values
            String name = traffic_name.getText();
            int intersectionId = Integer.parseInt(traffic_intersection_id.getText());
            int state = Integer.parseInt(traffics_state.getText());
            int waitTime = Integer.parseInt(traffics_waittime.getText());

            // Create a new traffic light object and add it
            TrafficLight newTrafficLight = new TrafficLight(name, state, waitTime, intersectionId);
            ServiceTrafficLight service = new ServiceTrafficLight();
            service.addTrafficLight(newTrafficLight);
            showdata(); // Refresh the data
            clearTextFields();

            showAlert("Success", "Traffic Light added successfully!", AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numeric values where appropriate.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred while adding the traffic light.", AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void modifyTrafficLight() {
        try {
            TrafficLight selectedTrafficLight = traffic_table.getSelectionModel().getSelectedItem();

            // Ensure a traffic light is selected
            if (selectedTrafficLight == null) {
                showAlert("Selection Error", "Please select a traffic light to modify.", AlertType.ERROR);
                return;
            }

            // Get the current values of the selected traffic light
            int currentId = selectedTrafficLight.getId();
            int currentIntersectionId = selectedTrafficLight.getIdIntersection();
            int currentState = selectedTrafficLight.getState();
            int currentWaitTime = selectedTrafficLight.getWaitTime();
            String currentName = selectedTrafficLight.getName();

            // Use the input values only if they are not empty
            String name = traffic_name.getText().isEmpty() ? currentName : traffic_name.getText();
            int intersectionId = traffic_intersection_id.getText().isEmpty() ? currentIntersectionId : Integer.parseInt(traffic_intersection_id.getText());
            int state = traffics_state.getText().isEmpty() ? currentState : Integer.parseInt(traffics_state.getText());
            int waitTime = traffics_waittime.getText().isEmpty() ? currentWaitTime : Integer.parseInt(traffics_waittime.getText());

            // Create updated traffic light object
            TrafficLight updatedTrafficLight = new TrafficLight(currentId, name, state, waitTime, intersectionId, currentIntersectionId, 0);
            ServiceTrafficLight service = new ServiceTrafficLight();
            service.updateTrafficLight(updatedTrafficLight);

            showdata();  // Refresh the displayed data
            clearTextFields();  // Clear the input fields

            showAlert("Success", "Traffic Light updated successfully!", AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numeric values where appropriate.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred while modifying the traffic light.", AlertType.ERROR);
        }
    }

    @FXML
    public void deleteTrafficLight() {
        try {
            TrafficLight selectedTrafficLight = traffic_table.getSelectionModel().getSelectedItem();
            if (selectedTrafficLight != null) {
                ServiceTrafficLight service = new ServiceTrafficLight();
                TrafficLight temp = new TrafficLight();
                temp.setId(selectedTrafficLight.getId());
                service.removeTrafficLight(temp);

                showdata();
                showAlert("Success", "Traffic Light deleted successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Selection Error", "No traffic light selected for deletion.", AlertType.WARNING);
            }
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred while deleting the traffic light.", AlertType.ERROR);
        }
    }

    private void clearTextFields() {
        traffic_id.clear();
        traffic_name.clear();
        traffic_intersection_id.clear();
        traffics_state.clear();
        traffics_waittime.clear();
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showdata(); // Load the data when the view is initialized
    }
}
