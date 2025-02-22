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

public class frontAffichProf {
    private user connectedUser;
    private profile userProfile;

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
    private Label role;
    @FXML
    private Label created_at;
    @FXML
    private Label updated_at;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button editPasswordButton;
    @FXML
    private ImageView to_home;
    @FXML
    private ImageView deconnexion;

    @FXML
    public void initialize() {
        editProfileButton.setOnAction(event -> navigateToEditProfile());
        editPasswordButton.setOnAction(event -> navigateToEditPassword());
        to_home.setOnMouseClicked(event -> navigateToHome());
        deconnexion.setOnMouseClicked(event -> logout());
        profileImage.setOnMouseClicked(event -> navigateToProfile());

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
        name.setText(userProfile.getName_u());
        username.setText(connectedUser.getUsername());
        email.setText(userProfile.getEmail_u());
        phone.setText(String.valueOf(userProfile.getPhone_u()));
        if (userProfile.getCreated_at() != null) {
            created_at.setText(userProfile.getCreated_at().toString());
        }
        if (userProfile.getUpdated_at() != null) {
            updated_at.setText(userProfile.getUpdated_at().toString());
        }

        // Charger les images
        loadImage(userProfile.getImage_u(), img_current_user);
        loadImage(userProfile.getImage_u(), profileImage);
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

    private void navigateToEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontUpdateProf.fxml"));
            Parent root = loader.load();

            frontUpdateProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) editProfileButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToEditPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontUpdatePass.fxml"));
            Parent root = loader.load();

            FrontUpdatePass controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) editPasswordButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
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
