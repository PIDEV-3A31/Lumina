package com.lumina.services;

import com.lumina.models.Intersection;
import com.lumina.models.TrafficLight;

import java.util.List;

public interface StatistiscIntersection {
        int AvrgWaitTimeInAllIntersction();

        Intersection TopCongestedIntersections();
        TrafficLight TopCongestedTrafficLights();

        List<TrafficLight> Top10CongestedTrafficLights();
        List<Intersection> Top10Intersections();

        List<Integer> AvrgWaitingTimeDuringSession();

}