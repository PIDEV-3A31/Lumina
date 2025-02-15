package com.lumina.main;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;
import com.lumina.services.ServiceIntersection;
import com.lumina.services.ServiceTrafficLight;
import com.lumina.services.ServiceStats;

import java.util.List;
import java.util.Scanner;

public class TLM {

    public static void TLMMain(String[] args) {
        // Create instances of the services for Intersection, TrafficLight, and Stats
        ServiceIntersection serviceIntersection = new ServiceIntersection();
        ServiceTrafficLight serviceTrafficLight = new ServiceTrafficLight();
        ServiceStats serviceStats = new ServiceStats();

        // ## Create and Add Intersection ##
        // Create a mock Intersection object
        Intersection intersection = new Intersection();
        intersection.setName("Main and 1st Avenue");
        intersection.setLongitude(9);
        intersection.setLatitude(2);
        intersection.setTrafficDensity(0.75f);
        intersection.setCreatedById(1); // Assume user ID is 1 (could be dynamic)

        // Add intersection to the database
        try {
            serviceIntersection.addIntersection(intersection);  // Add to the database
            System.out.println("Intersection added with ID: " + intersection.getId());  // Log the added intersection ID
        } catch (Exception e) {
            System.err.println("Error adding intersection: " + e.getMessage());  // Log any error encountered
        }

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");
        intersection.setId(31);  // Set a mock ID for testing

        // ## Create and Add Traffic Light ##
        // Create a mock Traffic Light object
        TrafficLight trafficLight = new TrafficLight();
        trafficLight.setName("Main Street Light");
        trafficLight.setDomain("Main Street");
        trafficLight.setState(1);  // Set state to Green light (1)
        trafficLight.setIdIntersection(intersection.getId()); // Associate traffic light with intersection
        trafficLight.setNumberOfCars(15);  // Set a mock number of cars for testing

        // Add traffic light to the database
        try {
            serviceTrafficLight.addTrafficLight(trafficLight);  // Add to the database
            System.out.println("Traffic light added: " + trafficLight.getName() + " to intersection ID " + intersection.getId());  // Log the added traffic light
        } catch (Exception e) {
            System.err.println("Error adding traffic light: " + e.getMessage());  // Log any error encountered
        }

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        System.out.println("## next test ##");

        // ## Print Intersection and Traffic Light details ##
        // Log the intersection and traffic light details before any updates
        System.out.println("Before update:");
        System.out.println(serviceIntersection.getIntersectionById(intersection.getId()));  // Get and log the intersection
        System.out.println(serviceTrafficLight.getTrafficLightById(trafficLight.getId()));  // Get and log the traffic light

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        // ## Update Traffic Light and Intersection ##
        // Update the traffic light state and intersection traffic density
        trafficLight.setState(0);  // Set the traffic light to Red (0)
        intersection.setTrafficDensity(0.85f);  // Update traffic density
        trafficLight.setNumberOfCars(25);  // Update the number of cars at the traffic light

        // Update the traffic light and intersection in the database
        serviceTrafficLight.updateTrafficLightState(trafficLight);  // Update the traffic light state
        serviceIntersection.updateIntersection(intersection);  // Update the intersection data

        // Print updated intersection and traffic light details
        System.out.println("After update:");
        System.out.println(serviceIntersection.getIntersectionById(intersection.getId()));  // Get and log the updated intersection
        System.out.println(serviceTrafficLight.getTrafficLightById(trafficLight.getId()));  // Get and log the updated traffic light

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        System.out.println("## next test ##");

        // ## Remove Intersection and Traffic Light ##
        // Uncomment the lines below to test the removal of the intersection and associated traffic light

        // Test: Removing intersection and associated traffic light
        // serviceIntersection.removeIntersection(intersection);  // Remove the intersection
        // serviceTrafficLight.removeTrafficLight(trafficLight);  // Remove the traffic light

        // Confirm removal (uncomment lines above to test actual removal)
        // System.out.println("Intersection and Traffic light removed." + intersection.getId());

        // ## Cascade Removal Test ##
        // Try removing the intersection (and cascade delete traffic lights if applicable)
        System.out.println("Remove cascade test");

        try {
            // Remove the intersection (traffic lights should be removed automatically if cascade delete is set)
            serviceIntersection.removeIntersection(intersection);  // Remove intersection and its related data (traffic lights)
            System.out.println("Intersection removed: " + intersection.getId());
        } catch (Exception e) {
            System.err.println("Error removing intersection: " + e.getMessage());  // Log any errors during removal
        }

        // ## Stats Testing ##
        // Create instances of the stats service (for testing service methods)
        serviceStats = new ServiceStats();

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Average Wait Time in All Intersections
        System.out.println("Testing Average Wait Time in All Intersections...");
        int avgWaitTime = serviceStats.AvrgWaitTimeInAllIntersction();
        System.out.println("Average Wait Time in All Intersections: " + avgWaitTime);
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Top Congested Intersection
        System.out.println("Testing Top Congested Intersection...");
        Intersection topCongestedIntersection = serviceStats.TopCongestedIntersections();
        if (topCongestedIntersection != null) {
            System.out.println("Top Congested Intersection: " + topCongestedIntersection.getId() + topCongestedIntersection.getName());
        } else {
            System.out.println("No congested intersections found.");
        }
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Top Congested Traffic Light
        System.out.println("Testing Top Congested Traffic Light...");
        TrafficLight topCongestedTrafficLight = serviceStats.TopCongestedTrafficLights();
        if (topCongestedTrafficLight != null) {
            System.out.println("Top Congested Traffic Light: " + topCongestedTrafficLight.getName());
        } else {
            System.out.println("No congested traffic lights found.");
        }
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Top 10 Congested Traffic Lights
        System.out.println("Testing Top 10 Congested Traffic Lights...");
        List<TrafficLight> top10CongestedTrafficLights = serviceStats.Top10CongestedTrafficLights();
        if (top10CongestedTrafficLights.isEmpty()) {
            System.out.println("No congested traffic lights found.");
        } else {
            for (TrafficLight light : top10CongestedTrafficLights) {
                System.out.println("Congested Traffic Light: " + light.getName());
            }
        }
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Top 10 Intersections
        System.out.println("Testing Top 10 Intersections...");
        List<Intersection> top10Intersections = serviceStats.Top10Intersections();
        if (top10Intersections.isEmpty()) {
            System.out.println("No intersections found.");
        } else {
            for (Intersection inter : top10Intersections) {
                System.out.println("Intersection: " + inter.getName());
            }
        }
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Test: Average Waiting Time During Session
        System.out.println("Testing Average Waiting Time During Session...");
        List<Integer> avgWaitingTimes = serviceStats.AvrgWaitingTimeDuringSession();
        if (avgWaitingTimes.isEmpty()) {
            System.out.println("No data available for average waiting times during session.");
        } else {
            for (int waitingTime : avgWaitingTimes) {
                System.out.println("Average Waiting Time: " + waitingTime);
            }
        }
        waitForUserInput("Press Enter to finish the tests...");

        System.out.println("Testing complete.");
    }

    // Helper method to wait for user input before continuing
    private static void waitForUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        scanner.nextLine(); // Wait for the user to press Enter
    }
}
