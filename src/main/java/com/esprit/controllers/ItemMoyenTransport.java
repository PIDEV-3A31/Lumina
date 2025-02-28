package com.esprit.controllers;

import com.esprit.models.moyenTransport;
import com.esprit.models.reservation;
import com.esprit.services.serviceReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class ItemMoyenTransport {

    @FXML
    private Label place_disponibleLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label type_transportLabel;


    @FXML
    private Button reserverButton;

    private moyenTransport moyen;

    public void setData(moyenTransport moyen) {
        this.moyen = moyen;
        updateLabels();
    }

    private void updateLabels() {
        if (moyen != null) {
            type_transportLabel.setText(moyen.getTypeTransport());
            place_disponibleLabel.setText("Places: " + (moyen.getCapaciteMax() - moyen.getPlace_reservees()));
            prixLabel.setText("Prix: " + " TND");
        }
    }

    @FXML
    public void reserver() throws IOException {
        if (moyen != null) {
            double prix = 50;
            StripePayement.setMontant(prix);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StripePayement.fxml"));
            Parent root = loader.load();

            StripePayement controller = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Attend la fermeture de la fenêtre

            if (controller.isPaymentSuccessful()) {
                reservation reservation = new reservation(1, 1, moyen.getIdMoyenTransport(), new Date(), 1, 5, "Confirmée");
                serviceReservation reservationService = new serviceReservation();
                reservationService.ajouter(reservation);

                moyen.setPlace_reservees(moyen.getPlace_reservees() + 1);

                System.out.println("Réservation confirmée !");
                updateLabels();
            } else {
                System.out.println("Le paiement a échoué, la réservation n'a pas été effectuée.");
            }
        } else {
            System.out.println("Aucun moyen de transport sélectionné !");
        }
    }




}
