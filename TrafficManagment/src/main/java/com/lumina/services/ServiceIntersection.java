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
        String query = "SELECT * FROM intersection WHERE id = " + id;  // Simple SQL query to select intersection by id

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Process the result set
            if (resultSet.next()) {
                intersection = new Intersection();
                intersection.setId(resultSet.getInt("id"));
                intersection.setLongitude(resultSet.getInt("longitude"));
                intersection.setLatitude(resultSet.getInt("latitude"));
                intersection.setTrafficDensity(resultSet.getFloat("traffic_density"));
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Log any errors
        }

        return intersection;  // Return the found intersection, or null if not found
    }



    @Override
    public List<Intersection> getAllIntersection() {
        List<Intersection> intersections = new ArrayList<>();
        String query = "SELECT * FROM intersection";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Intersection intersection = new Intersection(rs.getInt("longitude"), rs.getInt("latitude"), rs.getFloat("traffic_density"));
                intersection.setId(rs.getInt("id"));

                // Fetch and set the associated traffic lights
                /*Not set yet*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return intersections;
    }

    @Override
    public void setIntersection(Intersection intersection) {
        // This method could be used to update the intersection properties
    }

    @Override
    public void addIntersection(Intersection intersection) {
        String query = "INSERT INTO intersection(`name`,`longitude`, `latitude`) VALUES ('"+intersection.getName()+"','"+intersection.getLongitude()+"','"+intersection.getLatitude()+"')";
        try{
            connection.createStatement().executeUpdate(query);
            System.out.println("Intersection added");
        }
        catch(Exception e){

        }
    }


    @Override
    public void removeIntersection(Intersection intersection) {
        String deleteIntersectionQuery = "DELETE FROM intersection WHERE id = ?";
        String deleteLinksQuery = "DELETE FROM intersection WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteLinksQuery)) {
            // First, delete the links between traffic lights and the intersection
            stmt.setInt(1, intersection.getId());
            stmt.executeUpdate();

            // Then, delete the intersection itself
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteIntersectionQuery)) {
                deleteStmt.setInt(1, intersection.getId());
                deleteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateIntersection(Intersection intersection) {
        String query = "UPDATE intersection SET `name` = '" + intersection.getName() +
                "', `longitude` = " + intersection.getLongitude() +
                ", `latitude` = " + intersection.getLatitude() +
                ", `traffic_density` = " + intersection.getTrafficDensity() +
                " WHERE `id` = " + intersection.getId();

        try {
            Statement stmt = connection.createStatement();
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Intersection updated successfully.");
            } else {
                System.out.println("No intersection found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating intersection: " + e);
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
            e.printStackTrace();
        }

        return lights;
    }
}
