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

            double prix = 50; // Suppose que l'objet `moyen` a un prix défini

            StripePayement.setMontant(prix);  // Passer le prix au contrôleur StripePayement

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StripePayement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            reservation reservation = new reservation(1, 1, moyen.getIdMoyenTransport(), new Date(), 1, 5, "Confirmée");
            serviceReservation reservationService = new serviceReservation();
            reservationService.ajouter(reservation);


            moyen.setPlace_reservees(moyen.getPlace_reservees() + 1);


            System.out.println("Moyen de transport réservé : " + moyen.getIdTransport());

            updateLabels();  

        } else {
            System.out.println("Aucun moyen de transport sélectionné !");
        }
    }


}
