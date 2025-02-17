package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.esprit.models.ligneTransport;

public class ItemLigneTransport {

    @FXML
    private Label horaire_arrivee_label;

    @FXML
    private Label horaire_depart_label;

    @FXML
    private Label lieux_departLabel;

    @FXML
    private Label lieux_arriveLabel;

    @FXML
    private Label tarif_label;

    private ligneTransport ligneTransport;
    private InterfaceUserTransport mainController;

    /**
     * Initialise les données de l'élément
     */
    public void setData(ligneTransport ligneTransport, InterfaceUserTransport mainController) {
        this.ligneTransport = ligneTransport;
        this.mainController = mainController;

        lieux_departLabel.setText(ligneTransport.getLieuxDepart());
        lieux_arriveLabel.setText(ligneTransport.getLieuxArrive());
        horaire_depart_label.setText(ligneTransport.getHoraireDepart().toString());
        horaire_arrivee_label.setText(ligneTransport.getHoraireArrivee().toString());
        tarif_label.setText(String.valueOf(ligneTransport.getTarif()));
    }

    /**
     * Lorsque l'utilisateur clique sur un élément, affiche ses détails
     */
    @FXML
    private void onClickListener(MouseEvent event) {
        if (mainController != null) {
            mainController.afficherDetailsLigneTransport(ligneTransport);
        }
    }
}
