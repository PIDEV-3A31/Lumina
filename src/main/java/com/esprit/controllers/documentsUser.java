package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.Date;
import com.esprit.services.ServiceDemande;
import com.esprit.models.Demandes;

public class documentsUser {

    @FXML
    private ComboBox<String> type_document_label;
    @FXML
    private Button button_add;
    @FXML
    private TextField description_label;

    private ServiceDemande serviceDemande = new ServiceDemande();  // Instance du service

    @FXML
    public void initialize() {
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
        demande.setStatut_demande("En attente");
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



}
