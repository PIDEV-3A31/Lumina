package com.esprit.controllers.Document;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

        titreDocumentLabel.setPickOnBounds(true);
        descriptionLabel.setPickOnBounds(true);
        typeLabel.setPickOnBounds(true);
    }

    /**
     * Lorsque l'utilisateur clique sur un élément, affiche ses détails
     */
    @FXML
    private void onClickListener() {
        //System.out.println("Clic détecté sur : " + (document != null ? document.getTitre() : "Aucun document"));
        if (mainController != null) {
            mainController.afficherDetailsDocument(document);
        }
    }

}
