package com.esprit.controllers.Document;

import com.esprit.models.Demandes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class itemDemande {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label titreDocumentLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Button date_demande;

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
        titreDocumentLabel.setText(demandes.getType_document());
        descriptionLabel.setText(demandes.getDescription());
        typeLabel.setText(demandes.getStatut_demande());
        date_demande.setText(demandes.getDate_demande().toString());

        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(160);



        titreDocumentLabel.setPickOnBounds(true);
        descriptionLabel.setPickOnBounds(true);
        typeLabel.setPickOnBounds(true);
    }


}
