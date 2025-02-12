package com.lumina.services;
import com.lumina.models.Intersection;

import java.util.List;

public interface CrudIntersection <T> {
    T getIntersectionById(int id);
    List<T> getAllIntersection();
    void setIntersection(T trafficLight);
    void addIntersection(T trafficLight);
    void removeIntersection(T trafficLight);
    void updateIntersection(T trafficLight);




}
