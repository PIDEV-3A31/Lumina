package com.esprit.controllers;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import com.esprit.services.ServiceDemande;
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
    private ComboBox<String> type_document_label;

    @FXML
    private ImageView back;

    @FXML
    private Button valider_button;

    private ServiceDocuments serviceDocument = new ServiceDocuments();
    private Demandes demande;
    private Documents nouveauDocument;

    @FXML
    private void initialize() {

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

        back.setOnMouseClicked(event -> handleBack());



        // Associer les boutons à leurs actions
        button_add.setOnAction(event -> ajouter());
        file_chooser.setOnAction(event -> choisirFichier());

    }

    @FXML
    private void handleBack() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) back.getScene().getWindow();

            // Charger la première interface (ex: AfficherDocuments.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherDocuments.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle par la scène de la première interface
            Scene previousScene = new Scene(root);
            currentStage.setScene(previousScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        type_document_label.setValue(null);

    }

    public void setDemande(Demandes demande) {
        this.demande = demande;
    }

    // Cette méthode peut être appelée après la création du document
    public void setNouveauDocument(Documents document) {
        this.nouveauDocument = document;
    }

    public Documents getNouveauDocument() {
        return nouveauDocument;
    }

    // Cette méthode pourrait être utilisée pour créer le document
    @FXML
    private void handleSaveDocument() {
        // Créer et enregistrer le document, en s'assurant que l'objet est bien créé
        if (nouveauDocument == null) {
            nouveauDocument = new Documents();
            // Remplir les informations de `nouveauDocument` à partir de l'interface
            // Exemple : nouveauDocument.setTitre(titreTextField.getText());
            // Une fois créé, le document peut être enregistré avec la méthode ajouter()
            serviceDocument.ajouter(nouveauDocument);
            setNouveauDocument(nouveauDocument);  // Assurez-vous que `nouveauDocument` est bien défini
        }
    }



}
