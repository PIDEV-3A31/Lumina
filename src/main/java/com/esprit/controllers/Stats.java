package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Stats {

    @FXML
    private PieChart pieChart; // Doit correspondre à l'ID dans stats.fxml

    public void initialize() {
        // Vérifier si pieChart est bien lié
        if (pieChart == null) {
            System.out.println("Erreur : pieChart est null !");
            return;
        }

        // Données pour le graphique
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Industrie lourde", 40),
                new PieChart.Data("Transport", 30),
                new PieChart.Data("Énergie", 20),
                new PieChart.Data("Autres", 10)
        );

        // Ajouter les données au PieChart
        pieChart.setData(data);
    }
}
