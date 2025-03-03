package com.esprit.controllers;

import com.esprit.models.moyenTransport;
import com.esprit.services.serviceMoyenTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterMoyenTransport {

    @FXML
    private Button button_add;

    @FXML
    private TextField capaciteMax_label;

    @FXML
    private TextField etat_label;

    @FXML
    private TextField idLigne_label;

    @FXML
    private TextField immatriculation_label;

    @FXML
    private TextField typeTransport_label;

    @FXML
    private ImageView return_consulterMoyen;

    private serviceMoyenTransport serviceMoyenTransport = new serviceMoyenTransport();

    @FXML
    private void initialize() {
        // Associer le bouton à son action
        button_add.setOnAction(event -> ajouterMoyenTransport());
        return_consulterMoyen.setOnMouseClicked(event -> retourConsulterLigneTransport());


    }

    @FXML
    private void ajouterMoyenTransport() {
        String immatriculation = immatriculation_label.getText();
        String typeTransport = typeTransport_label.getText();
        String etat = etat_label.getText();

        // Vérifier si les champs numériques sont valides
        int capaciteMax;
        int idLigne;
        try {
            capaciteMax = Integer.parseInt(capaciteMax_label.getText());
            idLigne = Integer.parseInt(idLigne_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Capacité Max et ID Ligne doivent être des nombres valides.");
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (immatriculation.isEmpty() || typeTransport.isEmpty() || etat.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Création du moyen de transport
        moyenTransport transport = new moyenTransport(idLigne, typeTransport, capaciteMax, immatriculation, etat);

        // Ajout du moyen de transport
        try {
            serviceMoyenTransport.ajouter(transport);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Moyen de transport ajouté avec succès !");
            viderChamps();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du moyen de transport.");
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void viderChamps() {
        immatriculation_label.clear();
        typeTransport_label.clear();
        capaciteMax_label.clear();
        etat_label.clear();
        idLigne_label.clear();
    }
    private void retourConsulterLigneTransport() {
        try {
            Stage currentStage = (Stage) return_consulterMoyen.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
