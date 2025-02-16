package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.services.ServiceParking;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ajoutparking {

    @FXML
    private TextField name_parck_label;

    @FXML
    private TextField adresse_label;

    @FXML
    private TextField capacity_label;

    @FXML
    private TextField place_reservees_label;

    @FXML
    private TextField status_parking_label;

    @FXML
    private TextField tarif_label;

    @FXML
    private Button button_add_parking;

    private final ServiceParking serviceParking = new ServiceParking();

    @FXML
    private void initialize() {
        button_add_parking.setOnAction(event -> ajouter());
    }

    @FXML
    private void ajouter() {
        String name_parck = name_parck_label.getText();
        String adresses = adresse_label.getText();
        String status_parking = status_parking_label.getText();
        String capacityStr = capacity_label.getText();
        String placesReserveesStr = place_reservees_label.getText();
        String tarif = tarif_label.getText();  // Tarif est une chaîne selon ta classe

        // Vérifier si les champs sont remplis
        if (name_parck.isEmpty() || adresses.isEmpty() || status_parking.isEmpty() ||
                capacityStr.isEmpty() || placesReserveesStr.isEmpty() || tarif.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que les valeurs numériques sont valides
        int capacity, places_reservees;
        try {
            capacity = Integer.parseInt(capacityStr);
            places_reservees = Integer.parseInt(placesReserveesStr);
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Capacité et places réservées doivent être des nombres valides !");
            return;
        }

        // Création du parking (id_parck est auto-incrémenté donc non inclus)
        Parking parking = new Parking(name_parck, capacity, status_parking, adresses, tarif, places_reservees);

        // Ajout du parking
        serviceParking.ajouter(parking);
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Parking ajouté avec succès !");

        // Vider les champs après ajout
        viderChamps();
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
        name_parck_label.clear();
        adresse_label.clear();
        capacity_label.clear();
        place_reservees_label.clear();
        status_parking_label.clear();
        tarif_label.clear();
    }



}
