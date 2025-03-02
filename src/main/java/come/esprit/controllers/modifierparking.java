package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.services.ServiceParking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;

public class modifierparking {

    @FXML
    private Button button_modifier;

    @FXML
    private TextField nomparking_label;

    @FXML
    private TextField capacity_label;

    @FXML
    private TextField status_label;

    @FXML
    private TextField adresses_label;

    @FXML
    private TextField tariff_label; // Tarif est une chaîne de caractères

    private Parking currentParking;
    private final ServiceParking serviceParking = new ServiceParking();

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur ModifierParkingController");

        if (button_modifier == null) {
            System.out.println("ERREUR : button_modifier est NULL !");
        }

        button_modifier.setOnAction(event -> modifierparking());
    }

    // Méthode pour initialiser les valeurs du parking à modifier
    public void setParking(Parking parking) {
        this.currentParking = parking;

        // Remplir les champs avec les données du parking
        nomparking_label.setText(parking.getName_parck()); // Correction getter
        capacity_label.setText(String.valueOf(parking.getCapacity()));
        status_label.setText(parking.getStatus_parking()); // Correction getter
        adresses_label.setText(parking.getAdresses()); // Correction getter
        tariff_label.setText(parking.getTarif()); // Tarif est une chaîne
    }



    @FXML
    private void modifierparking() {
        String nomParking = nomparking_label.getText();
        String status = status_label.getText();
        String adresse = adresses_label.getText();
        String tariff = tariff_label.getText(); // Récupérer tarif en String

        // Vérifier si la capacité est un nombre valide
        int capacity;
        try {
            capacity = Integer.parseInt(capacity_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Capacité doit être un nombre valide.");
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (nomParking.isEmpty() || status.isEmpty() || adresse.isEmpty() || tariff.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Mise à jour du parking
        currentParking.setName_parck(nomParking);
        currentParking.setCapacity(capacity);
        currentParking.setStatus_parking(status);
        currentParking.setAdresses(adresse);
        currentParking.setTarif(tariff); // Tarif reste en String

        // Mise à jour dans la base de données
        try {
            serviceParking.modifier(currentParking);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Parking modifié avec succès !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du parking.");
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
