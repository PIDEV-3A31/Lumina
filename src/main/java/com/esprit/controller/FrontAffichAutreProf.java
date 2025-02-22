package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Objects;

public class FrontAffichAutreProf {
    private user connectedUser;
    private profile userProfile;
    private user selectedUser;
    private profile selectedProfile;

    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label username;
    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private Label created_at;
    @FXML
    private ImageView to_home;
    @FXML
    private ImageView deconnexion;

    @FXML
    public void initialize() {
        to_home.setOnMouseClicked(event -> navigateToHome());
        deconnexion.setOnMouseClicked(event -> logout());
        profileImage.setOnMouseClicked(event -> navigateToProfile());

        // Style du curseur
        to_home.setStyle("-fx-cursor: hand;");
        deconnexion.setStyle("-fx-cursor: hand;");
    }

    public void initData(user connectedUser, profile userProfile, user selectedUser, profile selectedProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.selectedUser = selectedUser;
        this.selectedProfile = selectedProfile;
        updateUI();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        name.setText(selectedProfile.getName_u());
        username.setText(selectedUser.getUsername());
        email.setText(selectedProfile.getEmail_u());
        phone.setText(String.valueOf(selectedProfile.getPhone_u()));
        if (selectedProfile.getCreated_at() != null) {
            created_at.setText(selectedProfile.getCreated_at().toString());
        }

        // Charger les images
        loadImage(userProfile.getImage_u(), img_current_user);
        loadImage(selectedProfile.getImage_u(), profileImage);
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
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAffichProf.fxml"));
            Parent root = loader.load();

            frontAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) name_current_user.getScene().getWindow();
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
}
