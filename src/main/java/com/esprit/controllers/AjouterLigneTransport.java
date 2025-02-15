package com.esprit.controllers;

import com.esprit.models.ligneTransport;
import com.esprit.services.serviceLigneTransport;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private ComboBox<Integer> hourDepartComboBox;

    @FXML
    private ComboBox<Integer> minuteDepartComboBox;

    @FXML
    private ComboBox<Integer> hourArriveeComboBox;

    @FXML
    private ComboBox<Integer> minuteArriveeComboBox;

    @FXML
    private TableColumn<ligneTransport, Void> supprimer_label;

    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur AjouterLigneTransport");

        // Vérifier si les composants sont bien chargés
        if (button_add == null) {
            System.out.println(" ERREUR : button_add est NULL !");
        }
        if (hourDepartComboBox == null || minuteDepartComboBox == null ||
                hourArriveeComboBox == null || minuteArriveeComboBox == null) {
            System.out.println(" ERREUR : Une ComboBox est NULL !");
            return;
        }

        // Remplir les ComboBox
        for (int i = 0; i <= 23; i++) {
            hourDepartComboBox.getItems().add(i);
            hourArriveeComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            minuteDepartComboBox.getItems().add(i);
            minuteArriveeComboBox.getItems().add(i);
        }

        // Définir des valeurs par défaut
        hourDepartComboBox.setValue(0);
        minuteDepartComboBox.setValue(0);
        hourArriveeComboBox.setValue(0);
        minuteArriveeComboBox.setValue(0);

        // Associer l'événement au bouton
        button_add.setOnAction(event -> ajouterLigneTransport());
    }

    @FXML
    private void ajouterLigneTransport() {
        String nomLigne = nomLigne_label.getText();
        String zoneCouverture = zoneCouverture_label.getText();
        String etat = etat_label.getText();

        // Vérifier si le tarif est un nombre valide
        double tarif;
        try {
            tarif = Double.parseDouble(tarif_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le tarif doit être un nombre valide.");
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (nomLigne.isEmpty() || zoneCouverture.isEmpty() || etat.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (hourDepartComboBox.getValue() == null || minuteDepartComboBox.getValue() == null ||
                hourArriveeComboBox.getValue() == null || minuteArriveeComboBox.getValue() == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un horaire valide !");
            return;
        }


        // Récupérer les valeurs des ComboBox
        int hourDepart = hourDepartComboBox.getValue();
        int minuteDepart = minuteDepartComboBox.getValue();
        int hourArrivee = hourArriveeComboBox.getValue();
        int minuteArrivee = minuteArriveeComboBox.getValue();

        // Convertir en format Time
        Time horaireDepart = Time.valueOf(String.format("%02d:%02d:00", hourDepart, minuteDepart));
        Time horaireArrivee = Time.valueOf(String.format("%02d:%02d:00", hourArrivee, minuteArrivee));

        // Création de la ligne de transport
        ligneTransport ligne = new ligneTransport(0, nomLigne, zoneCouverture, tarif, horaireDepart, horaireArrivee, etat);

        // Ajout dans la base de données
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
        hourDepartComboBox.setValue(0);
        minuteDepartComboBox.setValue(0);
        hourArriveeComboBox.setValue(0);
        minuteArriveeComboBox.setValue(0);
    }
}
