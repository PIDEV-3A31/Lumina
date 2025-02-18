package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.esprit.models.Documents;

public class ItemDocument {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label titreDocumentLabel;

    @FXML
    private Label typeLabel;

    private Documents document;
    private documentsUser mainController;

    /**
     * Initialise les données de l'élément
     */
    public void setData(Documents document, documentsUser mainController) {
        this.document = document;
        this.mainController = mainController;

        titreDocumentLabel.setText(document.getTitre());
        descriptionLabel.setText(document.getDescription());
        typeLabel.setText(document.getType_document());
    }

    /**
     * Lorsque l'utilisateur clique sur un élément, affiche ses détails
     */
    @FXML
    private void onClickListener(MouseEvent event) {
        if (mainController != null) {
            mainController.afficherDetailsDocument(document);
        }
    }
}
