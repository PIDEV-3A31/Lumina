package com.esprit.controllers;

import com.esprit.models.Documents;
import com.esprit.services.ServiceDocuments;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ModifierDocumentController {

    private Documents document;
    private ServiceDocuments serviceDocument = new ServiceDocuments();

    @FXML
    private Button button_add;

    @FXML
    private TextField description_label;

    @FXML
    private TextField fichier_label;

    @FXML
    private Button file_chooser;

    @FXML
    private TextField titre_label;

    @FXML
    private ComboBox<String> type_document_label;

    @FXML
    private ImageView back;

    @FXML
    private void initialize() {
        back.setOnMouseClicked(event -> handleBack());

        type_document_label.getItems().addAll(
                "Acte de naissance",
                "Carte d'identité nationale",
                "Permis de construire",
                "Certificat de résidence",
                "Extrait de mariage",
                "Certificat de décès",
                "Autorisation de commerce",
                "Extrait du registre de commerce"
        );

        // Associer les boutons à leurs actions
        button_add.setOnAction(event -> modifier());
        file_chooser.setOnAction(event -> choisirFichier());
    }

    // Set the document and service for modification
    public void setDocument(Documents document, ServiceDocuments serviceDocument) {
        this.document = document;
        this.serviceDocument = serviceDocument;

        // Pré-remplir les champs avec les données actuelles du document
        if (document != null) {
            titre_label.setText(document.getTitre());
            type_document_label.setValue(document.getType_document());
            description_label.setText(document.getDescription());
            fichier_label.setText(document.getChemin_fichier());
        }
    }

    // Handle save operation
    @FXML
    private void modifier() {
        String type_document = type_document_label.getValue();
        String titre = titre_label.getText();
        String description = description_label.getText();
        String chemin_fichier = fichier_label.getText();

        // Vérifier si les champs sont remplis
        if (titre.isEmpty() || description.isEmpty() || chemin_fichier.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification du titre (minimum 3 caractères, lettres et chiffres uniquement)
        if (!titre.matches("^[a-zA-Z0-9\\s]{3,50}$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le titre doit contenir entre 3 et 50 caractères (lettres et chiffres uniquement) !");
            return;
        }

        // Vérification de la description (minimum 10 caractères, pas de caractères spéciaux interdits)
        if (!description.matches("^[a-zA-Z0-9\\s.,'\"-]{10,500}$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit contenir entre 10 et 500 caractères !");
            return;
        }

        // Vérification du fichier (doit être un chemin valide avec une extension correcte)
        if (!chemin_fichier.matches("^.+\\.(pdf|docx|txt|jpg|png)$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le fichier doit être au format PDF, DOCX, TXT, JPG ou PNG !");
            return;
        }

        // Mettre à jour l'objet document avec les nouvelles valeurs
        document.setTitre(titre);
        document.setType_document(type_document);
        document.setDescription(description);
        document.setChemin_fichier(chemin_fichier);

        // Enregistrer les modifications dans la base de données
        serviceDocument.modifier(document);

        // Afficher un message de succès
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Document modifié avec succès!");


    }

    // Handle file selection
    @FXML
    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.docx", "*.txt","*.jpg","*.png"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            fichier_label.setText(file.getAbsolutePath());
        }
    }

    // Helper method to close the window
    private void closeWindow() {
        Stage stage = (Stage) button_add.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    // Display alert messages
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Navigate back to the previous scene
    @FXML
    private void handleBack() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) back.getScene().getWindow();

            // Charger la scène précédente (ex: AfficherDocuments.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherDocuments.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle par la scène de la première interface
            Scene previousScene = new Scene(root);
            currentStage.setScene(previousScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
