package com.esprit.controllers;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class itemDemande {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label titreDocumentLabel;

    @FXML
    private Label typeLabel;

    private Demandes demandes;
    private documentsUser mainController;


    public void setDataDemande(Demandes demandes, documentsUser mainController) {
        this.demandes = demandes;
        this.mainController = mainController;

        if (demandes != null) {
            System.out.println("Statut demande : " + demandes.getStatut_demande());
        } else {
            System.out.println("ERREUR : demandes est null !");
        }
        descriptionLabel.setText(demandes.getDescription());
        typeLabel.setText(demandes.getType_document());

        titreDocumentLabel.setPickOnBounds(true);
        descriptionLabel.setPickOnBounds(true);
        typeLabel.setPickOnBounds(true);
    }


}
