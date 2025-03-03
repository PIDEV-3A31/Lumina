package com.esprit.services;

import java.util.List;

public interface CrudIntersection <T> {
    T getIntersectionById(int id);
    List<T> getAllIntersection();
    void setIntersection(T trafficLight);
    void addIntersection(T trafficLight);
    void removeIntersection(T trafficLight);
    void updateIntersection(T trafficLight);




}
