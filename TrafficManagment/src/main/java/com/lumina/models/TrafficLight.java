package com.lumina.models;

public class TrafficLight {

    private int id;
    private String name;
    private String domain;
    private int state;
    private int waitTime;
    private int IdIntersection;

    // Default constructor

    public TrafficLight(int id , String name, String domain, int state, int waitTime,int IdIntersection) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.state = state;
        this.waitTime = waitTime;
        this.IdIntersection = IdIntersection;

    }

    public TrafficLight() {

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
}
