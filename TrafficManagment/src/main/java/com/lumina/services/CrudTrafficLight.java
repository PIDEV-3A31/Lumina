package com.lumina.services;
import com.lumina.models.TrafficLight;

import java.util.List;

public interface CrudTrafficLight <T>{
    void addTrafficLight(TrafficLight trafficLight);
    void removeTrafficLight(T trafficLight);
    void updateTrafficLightState(T trafficLight);
    List<T> getAllTrafficLight();
    T getTrafficLightById(int id);
    List<TrafficLight> getTrafficLightsByIntersectionId(int id);
}