package com.esprit.controllers.Transport;

import com.esprit.models.moyenTransport;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.serviceLigneTransport;
import com.esprit.services.serviceMoyenTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AjouterMoyenTransport {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private ComboBox<String> ligneComboBox;


    @FXML
    private Button button_add;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

    @FXML
    private TextField capaciteMax_label;

    @FXML
    private TextField etat_label;


    @FXML
    private TextField immatriculation_label;

    @FXML
    private TextField typeTransport_label;

    @FXML
    private ImageView return_consulterMoyen;

    @FXML
    private Label immatriculationErrorLabel;
    @FXML
    private Label typeTransportErrorLabel;
    @FXML
    private Label etatErrorLabel;
    @FXML
    private Label capaciteMaxErrorLabel;
    @FXML
    private Label ligneTransportErrorLabel;

    @FXML
    private ImageView OpenChatBot;

    private serviceMoyenTransport serviceMoyenTransport = new serviceMoyenTransport();

    private Map<Integer, String> lignesMap = new HashMap<>();
    private int selectedLigneId = -1;

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
    private void initialize() {
        OpenChatBot.setOnMouseClicked(event -> openChatBotWindow());
        chargerLignesTransport();
        // Associer le bouton à son action
        button_add.setOnAction(event -> ajouterMoyenTransport());
        return_consulterMoyen.setOnMouseClicked(event -> retourConsulterLigneTransport());


    }

    private void chargerLignesTransport() {
        serviceLigneTransport serviceLigne = new serviceLigneTransport();
        lignesMap = serviceLigne.getLignesMap(); // Récupérer les noms & IDs

        ligneComboBox.getItems().addAll(lignesMap.values()); // Ajouter les noms

        // Gérer la sélection pour récupérer l'ID correspondant
        ligneComboBox.setOnAction(event -> {
            String selectedNom = ligneComboBox.getValue();
            for (Map.Entry<Integer, String> entry : lignesMap.entrySet()) {
                if (entry.getValue().equals(selectedNom)) {
                    selectedLigneId = entry.getKey(); // Récupérer l'ID
                    break;
                }
            }
        });
    }


    @FXML
    private void ajouterMoyenTransport() {
        String immatriculation = immatriculation_label.getText();
        String typeTransport = typeTransport_label.getText();
        String etat = etat_label.getText();

        immatriculationErrorLabel.setText("");
        typeTransportErrorLabel.setText("");
        etatErrorLabel.setText("");
        capaciteMaxErrorLabel.setText("");
        ligneTransportErrorLabel.setText("");
        // Vérifier si un ID de ligne a été sélectionné
        if (selectedLigneId == -1) {
            ligneTransportErrorLabel.setText("Veuillez sélectionner une ligne de transport !");
        }

        int capaciteMax;
        try {
            capaciteMax = Integer.parseInt(capaciteMax_label.getText());
        } catch (NumberFormatException e) {
            capaciteMaxErrorLabel.setText("Capacité Max doit être un nombre valide.");
        }


        boolean hasError = false;
        if (immatriculation.isEmpty()) {
            immatriculationErrorLabel.setText("Immatriculation est obligatoire.");
            hasError = true;
        }
        if (typeTransport.isEmpty()) {
            typeTransportErrorLabel.setText("Type de transport est obligatoire.");
            hasError = true;
        }
        if (etat.isEmpty()) {
            etatErrorLabel.setText("État est obligatoire.");
            hasError = true;
        }

        if (hasError) {
            return;
        }





        // Vérifier si les champs numériques sont valides

        try {
            capaciteMax = Integer.parseInt(capaciteMax_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Capacité Max doit être un nombre valide.");
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (immatriculation.isEmpty() || typeTransport.isEmpty() || etat.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Création du moyen de transport
        moyenTransport transport = new moyenTransport(selectedLigneId, typeTransport, capaciteMax, immatriculation, etat);

        // Ajout du moyen de transport
        try {
            serviceMoyenTransport.ajouter(transport);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Moyen de transport ajouté avec succès !");
            viderChamps();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du moyen de transport.");
            e.printStackTrace();
        }
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
        immatriculation_label.clear();
        typeTransport_label.clear();
        capaciteMax_label.clear();
        etat_label.clear();
        ligneComboBox.getItems().clear();
    }
    private void retourConsulterLigneTransport() {
        try {
            Stage currentStage = (Stage) return_consulterMoyen.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();
            ConsulterLigneTransport controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
