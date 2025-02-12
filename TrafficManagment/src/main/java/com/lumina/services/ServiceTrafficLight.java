package com.lumina.services;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;
import com.lumina.utils.DataBase;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class ServiceTrafficLight implements CrudTrafficLight<TrafficLight> {

    private final Connection connection;

    public ServiceTrafficLight() {
        this.connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void addTrafficLight(TrafficLight trafficLight) {
        String query = "INSERT INTO `trafficlight`(`name`, `domain`, `state`, `intersectionID`) VALUES ('" + trafficLight.getName() + "','" + trafficLight.getDomain() + "','" + trafficLight.getState() + "','" + trafficLight.getIdIntersection() + "')";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Traffic light added");
        } catch (Exception e) {
            System.out.println("Error adding traffic light: " + e);
        }
    }

    @Override
    public void removeTrafficLight(TrafficLight trafficLight) {
        String query = "DELETE FROM trafficlight WHERE id='" + trafficLight.getId() + "'";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Traffic light removed");
        } catch (Exception e) {
            System.out.println("Error removing traffic light: " + e);
        }
    }

    @Override
    public void updateTrafficLightState(TrafficLight trafficLight) {
        try {
            // Construct the SQL query to update the state of the traffic light
            String query = "UPDATE trafficlight SET `state` = '" + trafficLight.getState() + "' WHERE `id` = " + trafficLight.getId();

            // Create a statement and execute the update
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Traffic light updated.");
        } catch (Exception e) {
            System.out.println("Error updating traffic light: " + e);
        }
    }

    @Override
    public List<TrafficLight> getAllTrafficLight() {
        List<TrafficLight> trafficLights = new ArrayList<>();
        String query = "SELECT * FROM trafficlight";  // Query to get all traffic lights
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Process each row from the result set
            while (resultSet.next()) {
                TrafficLight trafficLight = new TrafficLight();
                trafficLight.setId(resultSet.getInt("id"));  // Assuming the column name is 'id'
                trafficLight.setName(resultSet.getString("name"));
                trafficLight.setDomain(resultSet.getString("domain"));
                trafficLight.setState(resultSet.getInt("state"));
                trafficLight.setIdIntersection(resultSet.getInt("idIntersection"));
                // Add the traffic light to the list
                trafficLights.add(trafficLight);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all traffic lights: " + e);
        }
        return trafficLights;  // Return the list of traffic lights
    }

    @Override
    public TrafficLight getTrafficLightById(int id) {
        TrafficLight trafficLight = null;
        String query = "SELECT * FROM trafficlight WHERE id = ?";  // Query to get traffic light by id
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the 'id' parameter in the query
            statement.setInt(1, id);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // If a result is found, create the TrafficLight object
                if (resultSet.next()) {
                    trafficLight = new TrafficLight();
                    trafficLight.setId(resultSet.getInt("id"));
                    trafficLight.setName(resultSet.getString("name"));
                    trafficLight.setDomain(resultSet.getString("domain"));
                    trafficLight.setState(resultSet.getInt("state"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching traffic light by ID: " + e);
        }
        return trafficLight;  // Return the found traffic light, or null if not found
    }
}
