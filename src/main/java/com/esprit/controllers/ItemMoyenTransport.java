package com.esprit.controllers;

import com.esprit.models.moyenTransport;
import com.esprit.models.reservation;
import com.esprit.services.serviceReservation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Date;

public class ItemMoyenTransport {

    @FXML
    private Label place_disponibleLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label type_transportLabel;

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
    public void reserver() {
        if (moyen != null) {
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
