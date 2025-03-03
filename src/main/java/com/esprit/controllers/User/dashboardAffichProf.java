package com.esprit.controllers.User;

import com.esprit.controllers.loginn;
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

public class dashboardAffichProf {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_userconnecte;
    @FXML
    private ImageView img_userconnecte;
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
    private Button retour;
    @FXML
    private Button deconnexion;

    @FXML
    public void initialize() {
        editProfileButton.setOnAction(event -> navigateToEditProfile());
        editPasswordButton.setOnAction(event -> navigateToEditPassword());
        retour.setOnAction(event -> navigateBack());
        deconnexion.setOnAction(event -> logout());

    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
    }

    private void updateUI() {
        name_userconnecte.setText(userProfile.getName_u());
        username.setText(connectedUser.getUsername());
        name.setText(userProfile.getName_u());
        email.setText(userProfile.getEmail_u());
        phone.setText(String.valueOf(userProfile.getPhone_u()));
        role.setText(userProfile.getRole());
        if (userProfile.getCreated_at() != null) {
            created_at.setText(userProfile.getCreated_at().toString());
        }
        if (userProfile.getUpdated_at() != null) {
            updated_at.setText(userProfile.getUpdated_at().toString());
        }

        loadImage(userProfile.getImage_u(), img_userconnecte);
        loadImage(userProfile.getImage_u(), profileImage);
        System.out.println("test");
        System.out.println("Chemin de l'image: " + userProfile.getImage_u());

    }

    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                imageView.setImage(new Image("/" + imagePath));
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }


    private void navigateToEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifProf.fxml"));
            Parent root = loader.load();

            dashboardModifProf modifController = loader.getController();
            modifController.initDataForCurrentUser(connectedUser, userProfile);

            Stage stage = (Stage) editProfileButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToEditPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifPass.fxml"));
            Parent root = loader.load();

            dashboardModifPass passController = loader.getController();
            passController.initData(connectedUser, userProfile);

            Stage stage = (Stage) editPasswordButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            dashboardController controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) retour.getScene().getWindow();
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
