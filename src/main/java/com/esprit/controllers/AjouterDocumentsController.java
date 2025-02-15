package com.esprit.controllers;

import com.esprit.models.Documents;
import com.esprit.services.ServiceDocuments;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;

public class AjouterDocumentsController {

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
    private TextField type_document_label;

    private ServiceDocuments serviceDocument = new ServiceDocuments();

    @FXML
    private void initialize() {
        // Associer les boutons à leurs actions
        button_add.setOnAction(event -> ajouter());
        file_chooser.setOnAction(event -> choisirFichier());
    }

    @FXML
    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            fichier_label.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void ajouter() {
        String type_document = type_document_label.getText();
        String titre = titre_label.getText();
        String description = description_label.getText();
        String chemin_fichier = fichier_label.getText();

        // Vérifier si les champs sont remplis
        if (titre.isEmpty() || description.isEmpty() || chemin_fichier.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Création du document avec la date actuelle
        Documents document = new Documents(
                type_document,
                titre,
                description,
                Date.valueOf(LocalDate.now()),  // Date de création
                Date.valueOf(LocalDate.now()),  // Date de modification
                chemin_fichier
        );

        // Ajout du document
        serviceDocument.ajouter(document);
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", " Document ajouté avec succès !");

        // Vider les champs après ajout
        viderChamps();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void viderChamps() {
        titre_label.clear();
        description_label.clear();
        fichier_label.clear();
        type_document_label.clear();
    }
}
