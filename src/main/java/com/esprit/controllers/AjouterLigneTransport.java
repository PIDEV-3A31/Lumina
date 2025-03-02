package com.esprit.controllers;

import com.esprit.models.ligneTransport;
import com.esprit.services.serviceLigneTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;

public class AjouterLigneTransport {

    @FXML
    private Button button_add;

    @FXML
    private TextField nomLigne_label;

    @FXML
    private TextField zoneCouverture_label;

    @FXML
    private TextField tarif_label;

    @FXML
    private TextField etat_label;

    @FXML
    private TextField lieuxDepart_label;

    @FXML
    private TextField lieuxArrive_label;

    @FXML
    private ComboBox<Integer> hourDepartComboBox;

    @FXML
    private ComboBox<Integer> minuteDepartComboBox;

    @FXML
    private ComboBox<Integer> hourArriveeComboBox;

    @FXML
    private ComboBox<Integer> minuteArriveeComboBox;

    @FXML
    private ImageView return_consulterLigne;

    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();

    @FXML
    private void initialize() {
        for (int i = 0; i <= 23; i++) {
            hourDepartComboBox.getItems().add(i);
            hourArriveeComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            minuteDepartComboBox.getItems().add(i);
            minuteArriveeComboBox.getItems().add(i);
        }

        hourDepartComboBox.setValue(0);
        minuteDepartComboBox.setValue(0);
        hourArriveeComboBox.setValue(0);
        minuteArriveeComboBox.setValue(0);

        button_add.setOnAction(event -> ajouterLigneTransport());
        return_consulterLigne.setOnMouseClicked(event -> retourConsulterLigneTransport());
    }

    @FXML
    private void ajouterLigneTransport() {
        String nomLigne = nomLigne_label.getText();
        String zoneCouverture = zoneCouverture_label.getText();
        String etat = etat_label.getText();
        String lieuxDepart = lieuxDepart_label.getText();
        String lieuxArrive = lieuxArrive_label.getText();

        double tarif;
        try {
            tarif = Double.parseDouble(tarif_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le tarif doit être un nombre valide.");
            return;
        }

        if (nomLigne.isEmpty() || zoneCouverture.isEmpty() || etat.isEmpty() || lieuxDepart.isEmpty() || lieuxArrive.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        int hourDepart = hourDepartComboBox.getValue();
        int minuteDepart = minuteDepartComboBox.getValue();
        int hourArrivee = hourArriveeComboBox.getValue();
        int minuteArrivee = minuteArriveeComboBox.getValue();

        Time horaireDepart = Time.valueOf(String.format("%02d:%02d:00", hourDepart, minuteDepart));
        Time horaireArrivee = Time.valueOf(String.format("%02d:%02d:00", hourArrivee, minuteArrivee));

        ligneTransport ligne = new ligneTransport(0, nomLigne, zoneCouverture, tarif, horaireDepart, horaireArrivee, etat, lieuxDepart, lieuxArrive);

        try {
            serviceLigneTransport.ajouter(ligne);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Ligne de transport ajoutée avec succès !");
            viderChamps();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de la ligne de transport.");
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
        nomLigne_label.clear();
        zoneCouverture_label.clear();
        tarif_label.clear();
        etat_label.clear();
        lieuxDepart_label.clear();
        lieuxArrive_label.clear();
        hourDepartComboBox.setValue(0);
        minuteDepartComboBox.setValue(0);
        hourArriveeComboBox.setValue(0);
        minuteArriveeComboBox.setValue(0);
    }
    private void retourConsulterLigneTransport() {
        try {
            Stage currentStage = (Stage) return_consulterLigne.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
