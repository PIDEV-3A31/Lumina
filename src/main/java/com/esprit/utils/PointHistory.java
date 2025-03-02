package com.esprit.utils;

import com.esprit.models.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PointHistory {
    private static final String JSON_FILE_PATH = "src/main/resources/data/point_history.json";

    public static List<Point> loadPointHistory() {
        List<Point> pointHistoryList = new ArrayList<>();
        File file = new File(JSON_FILE_PATH);
        
        // Créer le répertoire si nécessaire
        file.getParentFile().mkdirs();
        
        try {
            if (file.exists()) {
                Reader reader = new FileReader(file);
                Type listType = new TypeToken<ArrayList<Point>>(){}.getType();
                Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
                pointHistoryList = gson.fromJson(reader, listType);
                reader.close();
                
                if (pointHistoryList == null) {
                    pointHistoryList = new ArrayList<>();
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier JSON: " + e.getMessage());
        }
        return pointHistoryList;
    }

    private static int getNextPointId() {
        List<Point> points = loadPointHistory();
        if (points.isEmpty()) {
            return 1;
        }
        // Trouver le plus grand ID et ajouter 1
        return points.stream()
                    .mapToInt(Point::getPointId)
                    .max()
                    .orElse(0) + 1;
    }

    public static void savePoint(Point point) {
        try {
            List<Point> pointHistoryList = loadPointHistory();
            
            // Définir l'ID auto-incrémenté
            point.setPointId(getNextPointId());
            
            pointHistoryList.add(point);

            File file = new File(JSON_FILE_PATH);
            Writer writer = new FileWriter(file);
            
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
                
            gson.toJson(pointHistoryList, writer);
            writer.close();
            
            System.out.println("Point enregistré avec succès: " + point);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement du point: " + e.getMessage());
        }
    }
}
