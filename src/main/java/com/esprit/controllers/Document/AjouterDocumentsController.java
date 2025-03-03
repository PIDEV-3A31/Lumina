package com.esprit.controllers.Document;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceDocuments;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class AjouterDocumentsController {
    private user connectedUser;
    private profile userProfile;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

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
    private Label errorDescription;

    @FXML
    private Label errorFichier;

    @FXML
    private Label errorTitre;
    @FXML
    private Label erreurtype;
    @FXML
    private ImageView OpenChatBot;


    @FXML
    private void initialize() {
        OpenChatBot.setOnMouseClicked(event -> openChatBotWindow());

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
    private void openChatBotWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatbotUi.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("ChatBot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) back.getScene().getWindow();

            // Charger la première interface (ex: AfficherDocuments.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherDocuments.fxml"));
            Parent root = loader.load();

            AfficherDocuments controller = loader.getController();
            controller.initData(connectedUser, userProfile);

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

        errorTitre.setText("");
        errorDescription.setText("");
        errorFichier.setText("");
        erreurtype.setText("");


        boolean hasError = false;

        if (titre.isEmpty()) {
            erreurtype.setText("Le titre est obligatoire !");
            hasError = true;

        }

        if (titre.isEmpty()) {
            errorTitre.setText("Le titre est obligatoire !");
            hasError = true;

        } else if (!titre.matches("^[a-zA-Z0-9\\s]{3,50}$")) {
            errorTitre.setText("Le titre doit contenir entre 3 et 50 caractères !");
            hasError = true;

        }

        if (description.isEmpty()) {
            errorDescription.setText("La description est obligatoire !");
            hasError = true;

        } else if (!description.matches("^[a-zA-Z0-9\\s.,'\"-]{10,1000}$")) {
            errorDescription.setText("La description doit contenir entre 10 et 1000 caractères !");
            hasError = true;

        }

        if (chemin_fichier.isEmpty()) {
            errorFichier.setText("Le fichier est obligatoire !");
            hasError = true;

        } else if (!chemin_fichier.matches("^.+\\.(pdf|docx|txt|jpg|png)$")) {
            errorFichier.setText("Le fichier doit être au format PDF, DOCX, TXT, JPG ou PNG !");
            hasError = true;

        }

        if (hasError) {
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
    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        if (userProfile.getImage_u() != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                img_current_user.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
        loadImage(userProfile.getImage_u(), img_current_user);
    }
    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + imagePath)).toExternalForm());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }



}
