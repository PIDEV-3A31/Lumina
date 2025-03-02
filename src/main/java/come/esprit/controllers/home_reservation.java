package come.esprit.controllers;

import come.esprit.models.Reservation;
import come.esprit.services.ServiceReservation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class home_reservation implements Initializable { // Ajout de Initializable

    @FXML
    private FlowPane vboxReservations;
    @FXML
    private ImageView return_ajouterparking;


    private final ServiceReservation serviceReservation = new ServiceReservation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("FXML chargé !");
        List<Reservation> reservations = serviceReservation.afficher(); // Assure-toi que cette méthode fonctionne
        afficherReservations(reservations);
        return_ajouterparking.setOnMouseClicked(event -> retourajouterParking());

    }

    public void afficherReservations(List<Reservation> reservations) {
        if (vboxReservations == null) {
            System.out.println("vboxReservations n'est pas initialisé !");
            return;
        }

        vboxReservations.getChildren().clear(); // Nettoyer avant d'ajouter

        for (Reservation reservation : reservations) {
            VBox card = new VBox();
            card.setPrefSize(250, 120);
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #0CBEB8; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

            Label idRes = new Label("N° Réservation: " + reservation.getId_reservation());
            idRes.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label idParck = new Label("Numéro du Parking: " + reservation.getId_parck());
            Label dateRes = new Label("Date: " + reservation.getDate_reservation());
            Label matricule = new Label("Matricule: " + reservation.getMatricule_voiture());



            card.getChildren().addAll(idRes, idParck, dateRes, matricule);
            vboxReservations.getChildren().add(card);
        }
    }

    private void retourajouterParking() {
        try {
            Stage currentStage = (Stage) return_ajouterparking.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_home.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            // Animation de fondu
            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();



            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
