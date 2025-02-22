package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Objects;

public class Parametres {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;
    @FXML
    private ToggleButton double_authentificator;
    @FXML
    private ImageView to_home;
    @FXML
    private ImageView deconnexion;

    @FXML
    public void initialize() {
        to_home.setOnMouseClicked(event -> navigateToHome());
        deconnexion.setOnMouseClicked(event -> logout());

        // Configuration du ToggleButton
        double_authentificator.setOnAction(event -> handle2FAToggle());

        // Style du curseur
        to_home.setStyle("-fx-cursor: hand;");
        deconnexion.setStyle("-fx-cursor: hand;");
    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        loadImage(userProfile.getImage_u(), img_current_user);
        
        // Initialiser l'état du ToggleButton
        double_authentificator.setSelected(connectedUser.isIs_2fa_enabled());
    }

    private void handle2FAToggle() {
        if (double_authentificator.isSelected()) {
            // Activer 2FA
            loginn loginController = new loginn();
            loginController.setup2FA(connectedUser.getId(), userProfile.getEmail_u());
        } else {
            // Désactiver 2FA
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.disable2FA(connectedUser.getId());
            connectedUser.setIs_2fa_enabled(false);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Two-factor authentication has been disabled.");
        }
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

            Stage stage = (Stage) to_home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
