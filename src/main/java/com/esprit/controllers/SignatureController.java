package com.esprit.controllers;

import com.esprit.services.HelloSignService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class SignatureController {

    @FXML
    private Label fileLabel;

    @FXML
    private Label statusLabel;  // Added status label to display signature status

    private File document;

    // Méthode pour choisir un fichier
    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        document = fileChooser.showOpenDialog(new Stage());

        if (document != null) {
            fileLabel.setText("Fichier sélectionné : " + document.getName());
        } else {
            fileLabel.setText("Aucun fichier sélectionné.");
        }
    }

    // Méthode pour signer le document
    @FXML
    public void signDocument() {
        if (document != null) {
            // Appel à HelloSign pour envoyer le document à signer
            HelloSignService service = new HelloSignService("628df1382f45dacaf2ef81e74551e23127362d54b1a0de5f0037bb1aea63c322", true);
            service.sendDocumentForSignature(document);
            statusLabel.setText("Document envoyé pour signature.");  // Provide feedback to the user
        } else {
            fileLabel.setText("Veuillez d'abord choisir un fichier.");
        }
    }

    // Méthode pour vérifier le statut de la signature
    @FXML
    public void checkSignatureStatus() {
        if (document != null) {
            HelloSignService service = new HelloSignService("628df1382f45dacaf2ef81e74551e23127362d54b1a0de5f0037bb1aea63c322", true);
            // Replace this with the actual signature request ID if you have it
            String requestId = "your-request-id-here";  // You'll need to set this dynamically
            String status = service.getSignatureStatus(requestId);
            statusLabel.setText(status);  // Display the status in the UI
        } else {
            fileLabel.setText("Veuillez d'abord choisir un fichier.");
        }
    }
}
