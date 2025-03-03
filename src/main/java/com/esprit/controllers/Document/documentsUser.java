package com.esprit.controllers.Document;

import com.esprit.controllers.frontHome;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceDocuments;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.esprit.services.ServiceDemande;
import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class documentsUser implements Initializable {
    private user connectedUser;
    private profile userProfile;
    @FXML
    private ComboBox<String> type_document_label;
    @FXML
    private Button button_add;
    @FXML
    private TextField description_label;

    @FXML
    private ScrollPane scroll;


    @FXML
    private ScrollPane scroll1;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private ImageView fichier_img;

    @FXML
    private ImageView return_home;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;


    private ServiceDemande serviceDemande = new ServiceDemande();  // Instance du service

    @FXML
    private ImageView OpenChatBot;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OpenChatBot.setOnMouseClicked(event -> openChatBotWindow());
        return_home.setOnMouseClicked(event -> navigateToHome());

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


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
                ajouterDemande(); // Appel de la méthode d'ajout

                // Afficher une alerte après l'ajout réussi
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Ajout effectué avec succès !");
                alert.showAndWait(); // Affiche l'alerte et attend la confirmation de l'utilisateur
            } catch (Exception e) {
                e.printStackTrace();

                // En cas d'erreur, afficher une alerte d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Ajout échoué");
                alert.setContentText("Une erreur s'est produite lors de l'ajout !");
                alert.showAndWait();
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
                itemController.setData(doc,this);

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid1.add(anchorPane, column++, row);
                grid1.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid1.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid1.setMaxWidth(Region.USE_PREF_SIZE);
                grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid1.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid1.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServiceDemande serviceDemande2 = new ServiceDemande();
        List<Demandes> demandesList = serviceDemande2.getDemandesByUserId(1);


        try {
            for (Demandes doc : demandesList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/itemDemande.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                itemDemande itemController = fxmlLoader.getController();
                itemController.setDataDemande(doc,this);

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
        grid.getChildren().clear();
        ServiceDemande serviceDemande2 = new ServiceDemande();
        List<Demandes> demandesList = serviceDemande2.getDemandesByUserId(1);

        int column = 0;
        int row = 1;
        try {
            for (Demandes doc : demandesList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/itemDemande.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                itemDemande itemController = fxmlLoader.getController();
                itemController.setDataDemande(doc,this);

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
                    fichier_img.setImage(image);
                    //System.out.println("ok");
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


    private void handleRedirection(MouseEvent event, String fxmlPath) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        // Récupérer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Changer la scène
        stage.setScene(scene);
        stage.show();
    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        System.out.println("jesuisla");
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


    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_home.fxml"));
            Parent root = loader.load();

            frontHome controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) return_home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
