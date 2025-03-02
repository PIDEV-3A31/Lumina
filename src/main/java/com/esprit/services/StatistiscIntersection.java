package com.esprit.services;

import com.esprit.models.Intersection;
import com.esprit.models.TrafficLight;

import java.util.List;

public interface StatistiscIntersection {
        int AvrgWaitTimeInAllIntersction();

        Intersection TopCongestedIntersections();
        TrafficLight TopCongestedTrafficLights();

        List<TrafficLight> Top10CongestedTrafficLights();
        List<Intersection> Top10Intersections();

        List<Integer> AvrgWaitingTimeDuringSession();

}