package com.esprit.controllers;

import com.esprit.services.ServiceDocuments;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.esprit.services.ServiceDemande;
import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;


public class documentsUser implements Initializable {

    @FXML
    private ComboBox<String> type_document_label;
    @FXML
    private Button button_add;
    @FXML
    private TextField description_label;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView fichier_img;

    private ServiceDemande serviceDemande = new ServiceDemande();  // Instance du service

    @Override
    public void initialize(URL location, ResourceBundle resources) {


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
        // Action associée au bouton d'ajout
        button_add.setOnAction(event -> {
            try {
                ajouterDemande();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ServiceDocuments serviceDocuments = new ServiceDocuments();
        List<Documents> documentsList = serviceDocuments.recupererDocumentsSelonIdUser(1);
        int column = 0;
        int row = 1;

        try {
            for (Documents doc : documentsList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/itemDocument.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemDocument itemController = fxmlLoader.getController();
                itemController.setData(doc,this); // Passer les données au contrôleur de l'item

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer les données et appeler le service d'ajout
    private void ajouterDemande() {
        // Récupérer les valeurs des champs
        String typeDocument = type_document_label.getValue();
        String description = description_label.getText();

        if (typeDocument == null || description.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }
        // Vérification de la description (minimum 10 caractères, pas de caractères spéciaux interdits)
        if (!description.matches("^[a-zA-Z0-9\\s.,'\"-]{10,500}$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit contenir entre 10 et 500 caractères !");
            return;
        }

        // Créer l'objet Demandes
        Demandes demande = new Demandes();
        demande.setId_utilisateur(1);
        demande.setId_document(1);
        demande.setType_document(typeDocument);
        demande.setDate_demande(new Date());
        demande.setStatut_demande("nouvelle");
        demande.setDescription(description);

        // Appeler la méthode ajouter du service
        serviceDemande.ajouter(demande);
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

        description_label.clear();
        type_document_label.setValue(null);

    }

    public void afficherDetailsDocument(Documents document) {
        // Vérifie si le document est valide
        if (document != null) {
            // Vérifie si le chemin du fichier n'est pas nul ou vide
            String cheminFichier = document.getChemin_fichier();
            if (cheminFichier != null && !cheminFichier.isEmpty()) {
                File file = new File(cheminFichier);
                if (file.exists()) {
                    // Charge l'image depuis le chemin du fichier
                    Image image = new Image(file.toURI().toString());
                    fichier_img.setImage(image);  // Affiche l'image dans l'ImageView
                } else {
                    System.out.println("Fichier image introuvable : " + cheminFichier);
                }
            } else {
                System.out.println("Le chemin du fichier est invalide.");
            }
        } else {
            System.out.println("Document invalide.");
        }
    }




    public void afficherImageDocument(Documents document) {
        if (document != null && document.getChemin_fichier() != null) {
            File file = new File(document.getChemin_fichier());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                fichier_img.setImage(image);
            } else {
                System.out.println("Fichier image introuvable : " + document.getChemin_fichier());
            }
        } else {
            System.out.println("Document ou chemin fichier invalide.");
        }
    }



}
