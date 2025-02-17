package com.lumina.models;


public class Intersection {
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private float longitude;
    private float latitude;
    private float traffic_density;
    private int CreatedById;


    public int getCreatedById() {
        return CreatedById;
    }

    public void setCreatedById(int createdById) {
        CreatedById = createdById;
    }


    public float getTraffic_density() {
        return traffic_density;
    }

    public void setTraffic_density(float traffic_density) {
        this.traffic_density = traffic_density;
    }

    public Intersection(String name, float longitude, float latitude, float traffic_density, int createdById) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.traffic_density = traffic_density;
        CreatedById = createdById;
    }

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
    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    // Getter and Setter for latitude
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
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
