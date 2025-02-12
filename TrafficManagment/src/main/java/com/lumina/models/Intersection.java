package com.lumina.models;

import java.util.List;
import java.util.ArrayList; // Add this import statement

public class Intersection {
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int longitude;
    private int latitude;
    private float traffic_density;

    // Constructor
    public Intersection(int longitude, int latitude, float traffic_density) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.traffic_density = traffic_density;
    }

    public Intersection() {

    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    // Getter and Setter for longitude
    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    // Getter and Setter for latitude
    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    // Getter and Setter for traffic_density
    public float getTrafficDensity() {
        return traffic_density;
    }

    public void setTrafficDensity(float traffic_density) {
        this.traffic_density = traffic_density;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", traffic_density=" + traffic_density +
                '}';
    }
}
