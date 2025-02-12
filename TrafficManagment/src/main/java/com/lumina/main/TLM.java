package com.lumina.main;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;
import com.lumina.services.ServiceIntersection;
import com.lumina.services.ServiceTrafficLight;
import java.util.Scanner;

public class TLM {

    public static void main(String[] args) {
        // Create instances of the services
        ServiceIntersection serviceIntersection = new ServiceIntersection();
        ServiceTrafficLight serviceTrafficLight = new ServiceTrafficLight();

        // Create mock Intersection
        Intersection intersection = new Intersection();
        intersection.setLongitude(9);
        intersection.setLatitude(2);
        intersection.setTrafficDensity(0.75f);

        // Add intersection to the database
        try {
            serviceIntersection.addIntersection(intersection);
            System.out.println("Intersection added: " + intersection.getId());  // Log the added intersection ID
        } catch (Exception e) {
            System.err.println("Error adding intersection: " + e.getMessage());  // Log any error
        }

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Create a mock Traffic Light
        TrafficLight trafficLight = new TrafficLight();
        trafficLight.setName("Main Street Light");
        trafficLight.setDomain("Main Street");
        trafficLight.setState(1);  // Green light
        trafficLight.setIdIntersection(16); // Link to the added intersection

        try {
            serviceTrafficLight.addTrafficLight(trafficLight);
            System.out.println("Traffic light added: " + trafficLight.getName() + " to intersection ID " + intersection.getId());  // Log traffic light added
        } catch (Exception e) {
            System.err.println("Error adding traffic light: " + e.getMessage());  // Log any error
        }

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        System.out.println("## next test ##");

        // Print intersection and traffic light details before any updates
        intersection.setId(10);
        trafficLight.setId(10);
        System.out.println("Before update:");
        System.out.println(serviceIntersection.getIntersectionById(intersection.getId()));
        System.out.println(serviceTrafficLight.getTrafficLightById(trafficLight.getId()));

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        // Update traffic light and intersection
        serviceTrafficLight.updateTrafficLightState(trafficLight);
        serviceIntersection.updateIntersection(intersection);

        // Print updated intersection and traffic light details
        System.out.println("After update:");
        System.out.println(serviceIntersection.getIntersectionById(intersection.getId()));
        System.out.println(serviceTrafficLight.getTrafficLightById(trafficLight.getId()));

        // Wait for user input to proceed to the next operation
        waitForUserInput("Press Enter to proceed to the next operation...");

        System.out.println("## next test ##");

        // Remove intersection and traffic light
        //serviceIntersection.removeIntersection(intersection);
        //serviceTrafficLight.removeTrafficLight(trafficLight);

        // Confirm removal
        //System.out.println("Intersection and Traffic light removed." + intersection.getId());

        System.out.println("Remove cascade test");
        intersection.setId(16);
        serviceIntersection.removeIntersection(intersection);
    }

    // Helper method to wait for user input before continuing
    private static void waitForUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        scanner.nextLine(); // Wait for the user to press Enter
    }
}
