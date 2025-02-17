package com.esprit.controllers;

import com.esprit.models.Demandes;
import com.esprit.services.ServiceDemande;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ajouterIdDocument {

    @FXML
    private TextField id_demande;

    @FXML
    private TextField id_document;

    @FXML
    private Button confirmerButton;

    private ServiceDemande serviceDemande;
    private Demandes demande;

    /**
     * Initialise le contrôleur avec une demande spécifique.
     */
    public void setDemande(Demandes demande, ServiceDemande serviceDemande) {
        this.demande = demande;
        this.serviceDemande = serviceDemande;

        // Pré-remplir les champs avec les valeurs existantes
        if (demande != null) {
            id_demande.setText(String.valueOf(demande.getId_demande()));
            id_demande.setEditable(false); // Empêcher la modification de l'ID de la demande
        }
    }

    @FXML
    private void initialize() {
        // Action du bouton confirmer
        confirmerButton.setOnAction(event -> handleConfirmer());
    }

    /**
     * Met à jour la demande avec l'ID du document saisi.
     */
    private void handleConfirmer() {
        if (demande != null) {
            try {
                int documentId = Integer.parseInt(id_document.getText());

                // Mettre à jour la demande avec l'ID du document
                demande.setId_document(documentId);
                demande.setStatut_demande("en cours");
                serviceDemande.modifier(demande);

                // Fermer la fenêtre après confirmation
                Stage stage = (Stage) confirmerButton.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                System.err.println("ID du document invalide.");
            }
        }
    }
}
