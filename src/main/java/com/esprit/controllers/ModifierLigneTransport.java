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

public class ModifierLigneTransport {

    @FXML
    private Button button_modifier;

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
    private ImageView return_consulterLigne;

    private ligneTransport currentLigne;
    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();

    @FXML
    private void handleConsulterTransport() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) return_consulterLigne.getScene().getWindow();

            // Charger la nouvelle interface en arrière-plan
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene newScene = new Scene(root);

            // Appliquer la nouvelle scène à la fenêtre actuelle avant de la fermer
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur ModifierLigneTransport");

        // Vérifier si les composants sont bien chargés
        if (button_modifier == null) {
            System.out.println(" ERREUR : button_modifier est NULL ! ");
        }
        if (hourDepartComboBox == null || minuteDepartComboBox == null ||
                hourArriveeComboBox == null || minuteArriveeComboBox == null) {
            System.out.println(" ERREUR : Une ComboBox est NULL ! ");
            return;
        }

        // Remplir les ComboBox avec les heures et minutes
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
        button_modifier.setOnAction(event -> modifierLigneTransport());

        return_consulterLigne.setOnMouseClicked(event -> { handleConsulterTransport(); });
    }

    // Méthode pour initialiser les valeurs de la ligne à modifier
    public void setLigneTransport(ligneTransport ligne) {
        this.currentLigne = ligne;

        // Remplir les champs avec les données de la ligne
        nomLigne_label.setText(ligne.getNomLigne());
        zoneCouverture_label.setText(ligne.getZoneCouverture());
        tarif_label.setText(String.valueOf(ligne.getTarif()));
        etat_label.setText(ligne.getEtat());

        // Remplir les ComboBox avec les heures et minutes de départ et d'arrivée
        hourDepartComboBox.setValue(ligne.getHoraireDepart().toLocalTime().getHour());
        minuteDepartComboBox.setValue(ligne.getHoraireDepart().toLocalTime().getMinute());
        hourArriveeComboBox.setValue(ligne.getHoraireArrivee().toLocalTime().getHour());
        minuteArriveeComboBox.setValue(ligne.getHoraireArrivee().toLocalTime().getMinute());
    }

    @FXML
    private void modifierLigneTransport() {
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

        // Mise à jour de la ligne de transport
        currentLigne.setNomLigne(nomLigne);
        currentLigne.setZoneCouverture(zoneCouverture);
        currentLigne.setTarif(tarif);
        currentLigne.setEtat(etat);
        currentLigne.setHoraireDepart(horaireDepart);
        currentLigne.setHoraireArrivee(horaireArrivee);

        // Mise à jour dans la base de données
        try {
            serviceLigneTransport.modifier(currentLigne);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Ligne de transport modifiée avec succès !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la modification de la ligne de transport.");
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
}
