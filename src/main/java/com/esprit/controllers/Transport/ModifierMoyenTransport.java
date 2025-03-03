package com.esprit.controllers.Transport;

import com.esprit.models.moyenTransport;
import com.esprit.models.profile;
import com.esprit.models.user;
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
import java.util.Objects;

public class ModifierMoyenTransport {
    private user connectedUser;
    private profile userProfile;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

    @FXML
    private Button button_update;

    @FXML
    private TextField capaciteMax_label;

    @FXML
    private TextField etat_label;

    @FXML
    private TextField idLigne_label;

    @FXML
    private TextField immatriculation_label;

    @FXML
    private ImageView return_consulterMoyen;

    @FXML
    private TextField typeTransport_label;

    private moyenTransport currentMoyen;
    private final serviceMoyenTransport serviceMoyenTransport = new serviceMoyenTransport();

    @FXML
    private void handleConsulterMoyenTransport() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) return_consulterMoyen.getScene().getWindow();

            // Charger la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();
            ConsulterLigneTransport controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            // Changer la scène actuelle
            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur ModifierMoyenTransport");

        // Vérifier si les composants sont bien chargés
        if (button_update == null) {
            System.out.println(" ERREUR : button_update est NULL ! ");
        }

        // Associer l'événement au bouton
        button_update.setOnAction(event -> modifierMoyenTransport());

        return_consulterMoyen.setOnMouseClicked(event -> handleConsulterMoyenTransport());
    }

    // Méthode pour initialiser les valeurs du moyen de transport à modifier
    public void setMoyenTransport(moyenTransport moyen) {
        this.currentMoyen = moyen;

        // Remplir les champs avec les données du moyen de transport
        immatriculation_label.setText(moyen.getImmatriculation());
        typeTransport_label.setText(moyen.getTypeTransport());
        capaciteMax_label.setText(String.valueOf(moyen.getCapaciteMax()));
        etat_label.setText(moyen.getEtat());
        idLigne_label.setText(String.valueOf(moyen.getIdLigne()));
    }

    @FXML
    private void modifierMoyenTransport() {
        String immatriculation = immatriculation_label.getText();
        String typeTransport = typeTransport_label.getText();
        String etat = etat_label.getText();
        int capaciteMax;
        int idLigne;

        // Vérifier si la capacité max est un nombre valide
        try {
            capaciteMax = Integer.parseInt(capaciteMax_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La capacité maximale doit être un nombre valide.");
            return;
        }

        // Vérifier si l'ID de la ligne est un nombre valide
        try {
            idLigne = Integer.parseInt(idLigne_label.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ID de la ligne doit être un nombre valide.");
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (immatriculation.isEmpty() || typeTransport.isEmpty() || etat.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Mise à jour du moyen de transport
        currentMoyen.setImmatriculation(immatriculation);
        currentMoyen.setTypeTransport(typeTransport);
        currentMoyen.setCapaciteMax(capaciteMax);
        currentMoyen.setEtat(etat);
        currentMoyen.setIdLigne(idLigne);

        // Mise à jour dans la base de données
        try {
            serviceMoyenTransport.modifier(currentMoyen);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Moyen de transport modifié avec succès !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du moyen de transport.");
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
