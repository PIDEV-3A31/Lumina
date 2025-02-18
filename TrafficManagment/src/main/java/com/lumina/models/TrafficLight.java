package com.lumina.models;

public class TrafficLight {

    private int id;
    private String name;
    private String domain;
    private int state;
    private int waitTime;
    private int IdIntersection;
    private int numberOfCars;
    private int CurrentWaitTime;

    public TrafficLight(int id, String name, int state, int waitTime, int intersectionId, int idIntersection, int numberOfCars) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.waitTime = waitTime;
        IdIntersection = intersectionId;
        this.numberOfCars = numberOfCars;

    }


    public int getCurrentWaitTime() {
        return CurrentWaitTime;
    }

    public void setCurrentWaitTime(int currentWaitTime) {
        CurrentWaitTime = currentWaitTime;
    }




    // Default constructor
    public TrafficLight() {}
    public TrafficLight(int id , String name, String domain, int state, int waitTime, int IdIntersection, int numberOfCars) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.state = state;
        this.waitTime = waitTime;
        this.IdIntersection = IdIntersection;


        this.numberOfCars = numberOfCars;
    }
    public TrafficLight( String name,  int state, int waitTime, int IdIntersection) {
        this.name = name;
        this.state = state;
        this.waitTime = waitTime;
        this.IdIntersection = IdIntersection;
        this.numberOfCars = 0;
        this.domain = "";

    }

    public TrafficLight(int numberOfCars) {

        this.numberOfCars = numberOfCars;
    }

    public int getIdIntersection() {
        return IdIntersection;
    }

    public void setIdIntersection(int idIntersection) {
        IdIntersection = idIntersection;
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", state=" + state +
                ", waitTime=" + waitTime +
                ", IdIntersection=" + IdIntersection +
                ", numberOfCars=" + numberOfCars +
                ", CurrentWaitTime=" + CurrentWaitTime +
                '}';
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for domain
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    // Getter and Setter for state
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    // Getter and Setter for waitTime
    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }
}
