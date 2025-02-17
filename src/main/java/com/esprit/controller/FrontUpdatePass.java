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

public class FrontUpdatePass {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Button affich_mdp1;

    @FXML
    private Button affich_mdp2;

    @FXML
    private Button affich_mdp3;

    @FXML
    private Button button_search;

    @FXML
    private PasswordField confirm_new_password;

    @FXML
    private ImageView deconnexion;

    @FXML
    private ImageView eye_icon1;

    @FXML
    private ImageView eye_icon2;

    @FXML
    private ImageView eye_icon3;

    @FXML
    private Label identfLabel;

    @FXML
    private ImageView img_current_user;

    @FXML
    private Label name_current_user;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField old_password;

    @FXML
    private ImageView parametres;

    @FXML
    private ImageView profile;

    @FXML
    private TextField textfield_search;

    @FXML
    private ImageView to_home;
    private boolean isOldPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @FXML
    private Button retour;
    @FXML
    private Button update_password;
    @FXML
    public void initialize() {
        update_password.setOnAction(event -> updatePassword());
        retour.setOnAction(event -> navigateBack());
        deconnexion.setOnMouseClicked(event -> logout());
        to_home.setOnMouseClicked(event -> navigateToHome());
        name_current_user.setOnMouseClicked(event -> navigateToProfile());
        profile.setOnMouseClicked(event -> navigateToProfile());

        // Initialiser les gestionnaires pour les boutons d'affichage
        affich_mdp1.setOnAction(event -> togglePasswordVisibility(old_password, affich_mdp1, eye_icon1, isOldPasswordVisible));
        affich_mdp2.setOnAction(event -> togglePasswordVisibility(new_password, affich_mdp2, eye_icon2, isNewPasswordVisible));
        affich_mdp3.setOnAction(event -> togglePasswordVisibility(confirm_new_password, affich_mdp3, eye_icon3, isConfirmPasswordVisible));
        to_home.setStyle("-fx-cursor: hand;");
        deconnexion.setStyle("-fx-cursor: hand;");
        name_current_user.setStyle("-fx-cursor: hand;");
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
        System.out.println("hani hne");
    }

    private void updatePassword() {
        if (!validateInputs()) {
            return;
        }

        try {
            ServiceUser serviceUser = new ServiceUser();
            user updatedUser = new user(connectedUser.getId(),
                    connectedUser.getUsername(), new_password.getText());
            serviceUser.modifer(updatedUser, connectedUser.getId());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe modifié avec succès!");
            navigateBack();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la modification du mot de passe: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (old_password.getText().isEmpty() || new_password.getText().isEmpty()
                || confirm_new_password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs!");
            return false;
        }

        if (!old_password.getText().equals(connectedUser.getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ancien mot de passe incorrect!");
            return false;
        }

        if (!new_password.getText().equals(confirm_new_password.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Le nouveau mot de passe et sa confirmation ne correspondent pas!");
            return false;
        }

        return true;
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void togglePasswordVisibility(PasswordField passwordField, Button button, ImageView eyeIcon, boolean isVisible) {
        if (!isVisible) {
            // Afficher le mot de passe
            String password = passwordField.getText();
            passwordField.setPromptText(passwordField.getText());
            passwordField.clear();
            passwordField.setStyle("-fx-text-inner-color: black;");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye.png").toExternalForm()));
        } else {
            // Cacher le mot de passe
            String password = passwordField.getPromptText();
            passwordField.setText(password);
            passwordField.setPromptText("");
            passwordField.setStyle("");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye-slash.png").toExternalForm()));
        }

        // Inverser l'état
        if (passwordField == old_password) {
            isOldPasswordVisible = !isOldPasswordVisible;
        } else if (passwordField == new_password) {
            isNewPasswordVisible = !isNewPasswordVisible;
        } else if (passwordField == confirm_new_password) {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        }
    }
    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }
    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAffichProf.fxml"));
            Parent root = loader.load();

            frontAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) retour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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
