package com.lumina.services;

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
        String query = "INSERT INTO `trafficlight`(`name`, `domain`, `state`, `intersectionID`, `numberofcars`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the parameters for the query
            statement.setString(1, trafficLight.getName());
            statement.setString(2, trafficLight.getDomain());
            statement.setInt(3, trafficLight.getState());
            statement.setInt(4, trafficLight.getIdIntersection());
            statement.setInt(5, trafficLight.getNumberOfCars());  // Set the new field 'numberofcars'

            // Execute the update
            statement.executeUpdate();
            System.out.println("Traffic light added");
        } catch (SQLException e) {
            System.out.println("Error adding traffic light: " + e);
        }
    }

    @Override
    public void removeTrafficLight(TrafficLight trafficLight) {
        String query = "DELETE FROM trafficlight WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the 'id' parameter in the query
            statement.setInt(1, trafficLight.getId());

            // Execute the update
            statement.executeUpdate();
            System.out.println("Traffic light removed");
        } catch (SQLException e) {
            System.out.println("Error removing traffic light: " + e);
        }
    }

    @Override
    public void updateTrafficLightState(TrafficLight trafficLight) {
        String query = "UPDATE trafficlight SET `state` = ?, `numberofcars` = ? WHERE `id` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the parameters for the query
            statement.setInt(1, trafficLight.getState());
            statement.setInt(2, trafficLight.getNumberOfCars());  // Update the new field 'numberofcars'
            statement.setInt(3, trafficLight.getId());

            // Execute the update
            statement.executeUpdate();
            System.out.println("Traffic light updated.");
        } catch (SQLException e) {
            System.out.println("Error updating traffic light: " + e);
        }
    }

    @Override
    public List<TrafficLight> getAllTrafficLight() {
        List<TrafficLight> trafficLights = new ArrayList<>();
        String query = "SELECT * FROM trafficlight";  // Query to get all traffic lights
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Process each row from the result set
            while (resultSet.next()) {
                TrafficLight trafficLight = new TrafficLight();
                trafficLight.setId(resultSet.getInt("id"));  // Assuming the column name is 'id'
                trafficLight.setName(resultSet.getString("name"));
                trafficLight.setDomain(resultSet.getString("domain"));
                trafficLight.setState(resultSet.getInt("state"));
                trafficLight.setIdIntersection(resultSet.getInt("intersectionID"));
                trafficLight.setNumberOfCars(resultSet.getInt("numberofcars"));  // Retrieve the new field 'numberofcars'
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
                    trafficLight.setIdIntersection(resultSet.getInt("intersectionID"));
                    trafficLight.setNumberOfCars(resultSet.getInt("numberofcars"));  // Retrieve the new field 'numberofcars'
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching traffic light by ID: " + e);
        }
        return trafficLight;  // Return the found traffic light, or null if not found
    }


    @Override
    public List<TrafficLight> getTrafficLightsByIntersectionId(int id) {
        List<TrafficLight> trafficLights = new ArrayList<>();  // List to store all traffic lights for this intersection
        String query = "SELECT * FROM trafficlight WHERE intersectionID = ?";  // Query to get traffic lights by intersectionID

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the 'id' parameter in the query
            statement.setInt(1, id);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Loop through all results (in case there are multiple traffic lights)
                while (resultSet.next()) {
                    TrafficLight trafficLight = new TrafficLight();
                    trafficLight.setId(resultSet.getInt("id"));
                    trafficLight.setName(resultSet.getString("name"));
                    trafficLight.setDomain(resultSet.getString("domain"));
                    trafficLight.setState(resultSet.getInt("state"));
                    trafficLight.setIdIntersection(resultSet.getInt("intersectionID"));
                    trafficLight.setNumberOfCars(resultSet.getInt("numberofcars"));  // Retrieve the 'numberofcars' field

                    // Add the created traffic light to the list
                    trafficLights.add(trafficLight);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching traffic lights by Intersection ID: " + e);
        }

        return trafficLights;  // Return the list of traffic lights, or an empty list if none are found
    }

}
