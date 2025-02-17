package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.services.ServiceParking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ajoutparking {

    @FXML
    private TextField name_parck_label;

    @FXML
    private TextField adresse_label;

    @FXML
    private ImageView back;

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

        back.setOnMouseClicked(event -> handleBack());
    }


   private void handleBack() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) back.getScene().getWindow();

            // Charger la première interface (ex: ListeParking.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeParking.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle par la scène de la première interface
            Scene previousScene = new Scene(root);
            currentStage.setScene(previousScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeParking.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) button_add_parking.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
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
        name_parck_label.clear();
        adresse_label.clear();
        capacity_label.clear();
        place_reservees_label.clear();
        status_parking_label.clear();
        tarif_label.clear();
    }



}
