package com.lumina.services;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;
import com.lumina.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceIntersection implements CrudIntersection<Intersection> {

    private Connection connection;

    public ServiceIntersection() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public Intersection getIntersectionById(int id) {
        Intersection intersection = null;
        String query = "SELECT * FROM intersection WHERE id = ?";  // Using PreparedStatement to avoid SQL injection

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                // Process the result set
                if (resultSet.next()) {
                    intersection = new Intersection();
                    intersection.setId(resultSet.getInt("id"));
                    intersection.setLongitude(resultSet.getFloat("longitude"));
                    intersection.setLatitude(resultSet.getFloat("latitude"));
                    intersection.setTrafficDensity(resultSet.getFloat("traffic_density"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error with proper logging
        }

        return intersection;  // Return the found intersection, or null if not found
    }



    @Override
    public List<Intersection> getAllIntersection() {
        List<Intersection> intersections = new ArrayList<>();
        String query = "SELECT * FROM intersection";  // Query to get all intersections

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Intersection intersection = new Intersection();
                intersection.setId(rs.getInt("id"));
                intersection.setName(rs.getString("name"));
                intersection.setLongitude(rs.getFloat("longitude"));
                intersection.setLatitude(rs.getFloat("latitude"));
                intersection.setTrafficDensity(rs.getFloat("traffic_density"));
                intersections.add(intersection);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error with proper logging
        }

        return intersections;
    }

    @Override
    public void setIntersection(Intersection intersection) {
        // Method for updating intersection properties if needed
        updateIntersection(intersection);  // Assuming this method will be used for updates
    }

    @Override
    public void addIntersection(Intersection intersection) {
        String query = "INSERT INTO intersection(`name`, `longitude`, `latitude`, `CreatedById`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, intersection.getName());
            statement.setDouble(2, intersection.getLongitude());
            statement.setDouble(3, intersection.getLatitude());
            statement.setInt(4, intersection.getCreatedById());

            // Execute the update
            statement.executeUpdate();
            System.out.println("Intersection added");
        } catch (SQLException e) {
            System.out.println("Error adding intersection: " + e.getMessage());
        }
    }

    @Override
    public void removeIntersection(Intersection intersection) {
        String deleteIntersectionQuery = "DELETE FROM intersection WHERE id = ?";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteIntersectionQuery)) {
            // Set the intersection ID to be deleted
            deleteStmt.setInt(1, intersection.getId());

            // Execute the deletion of the intersection
            int rowsAffected = deleteStmt.executeUpdate();

            // Check if any row was deleted
            if (rowsAffected > 0) {
                System.out.println("Intersection with ID " + intersection.getId() + " removed successfully.");
            } else {
                System.out.println("No intersection found with the given ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error removing intersection: " + e.getMessage());
        }
    }


    @Override
    public void updateIntersection(Intersection intersection) {
        String query = "UPDATE intersection SET `name` = ?, `longitude` = ?, `latitude` = ?, `traffic_density` = ? WHERE `id` = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set the parameters for the query
            stmt.setString(1, intersection.getName());
            stmt.setDouble(2, intersection.getLongitude());
            stmt.setDouble(3, intersection.getLatitude());
            stmt.setFloat(4, intersection.getTrafficDensity());
            stmt.setInt(5, intersection.getId());

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Intersection updated successfully.");
            } else {
                System.out.println("No intersection found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating intersection: " + e.getMessage());
        }
    }

    // Helper method to get traffic lights associated with an intersection
    private List<TrafficLight> getTrafficLightsForIntersection(int intersectionId) {
        List<TrafficLight> lights = new ArrayList<>();
        String query = "SELECT t.* FROM trafficlight t " +
                "JOIN intersection_traffic_light itl ON t.id = itl.traffic_light_id " +
                "WHERE itl.intersection_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, intersectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TrafficLight light = new TrafficLight();
                    light.setId(rs.getInt("id"));
                    light.setName(rs.getString("name"));
                    light.setDomain(rs.getString("domain"));
                    light.setState(rs.getInt("state"));
                    light.setWaitTime(rs.getInt("wait_time"));
                    lights.add(light);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error with proper logging
        }

        return lights;
    }
}
