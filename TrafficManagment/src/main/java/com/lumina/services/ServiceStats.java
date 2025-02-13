package com.lumina.services;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;
import com.lumina.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceStats implements StatistiscIntersection {

    private final Connection connection;

    public ServiceStats() {
        this.connection = DataBase.getInstance().getConnection();
    }

    @Override
    public int AvrgWaitTimeInAllIntersction() {
        String query = "SELECT SUM(t.numberofcars) / COUNT(t.id) AS avg_wait_time " +
                "FROM trafficlight t";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("avg_wait_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if no data
    }

    @Override
    public Intersection TopCongestedIntersections() {
        String query = "SELECT i.id, i.name, MAX(i.traffic_density) AS max_density FROM intersection i " ;
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Intersection intersection = new Intersection();
                intersection.setId(resultSet.getInt("id"));
                intersection.setName(resultSet.getString("name"));
                return intersection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no data
    }

    @Override
    public TrafficLight TopCongestedTrafficLights() {
        String query = "SELECT t.id, t.name, t.numberofcars AS total_cars " +
                "FROM trafficlight t " +
                "GROUP BY t.id, t.name " +
                "ORDER BY total_cars DESC " +
                "LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                TrafficLight trafficLight = new TrafficLight();
                trafficLight.setId(resultSet.getInt("id"));
                trafficLight.setName(resultSet.getString("name"));
                return trafficLight;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no data
    }

    @Override
    public List<TrafficLight> Top10CongestedTrafficLights() {
        List<TrafficLight> trafficLights = new ArrayList<>();
        String query = "SELECT t.id, t.name, t.numberofcars AS total_cars FROM trafficlight t " +
                "GROUP BY t.id, t.name " +
                "ORDER BY total_cars DESC " +
                "LIMIT 10";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                TrafficLight trafficLight = new TrafficLight();
                trafficLight.setId(resultSet.getInt("id"));
                trafficLight.setName(resultSet.getString("name"));
                trafficLights.add(trafficLight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trafficLights;
    }

    @Override
    public List<Intersection> Top10Intersections() {
        List<Intersection> intersections = new ArrayList<>();
        String query = "SELECT i.id, i.name, t.numberofcars FROM intersection i " +
                "JOIN trafficlight t ON i.id = t.intersectionID " +
                "ORDER BY t.numberofcars DESC " +
                "LIMIT 10";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Intersection intersection = new Intersection();
                intersection.setId(resultSet.getInt("id"));
                intersection.setName(resultSet.getString("name"));
                intersections.add(intersection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  intersections;
    }

    @Override
    public List<Integer> AvrgWaitingTimeDuringSession() {
        return List.of();
    }
    /*
            @Override
            public List<Integer> AvrgWaitingTimeDuringSession() {
                List<Integer> waitingTimes = new ArrayList<>();
                String query = "SELECT AVG(traffic_density * numberofcars) AS avg_wait_time " +
                        "FROM trafficlight t " +
                        "JOIN intersection i ON i.id = t.intersectionID " +
                        "WHERE t.state = 1";  // For example, consider only active state traffic lights
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        waitingTimes.add(resultSet.getInt("avg_wait_time"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return waitingTimes;
            }Trace();
        }
        return intersections;
    }*/



}
