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

public class dashboardModifPass {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_userconnecte;
    @FXML
    private ImageView img_userconnecte;
    @FXML
    private PasswordField old_password;
    @FXML
    private PasswordField new_password;
    @FXML
    private PasswordField confirm_new_password;
    @FXML
    private Button update_password;
    @FXML
    private Button retour;

    @FXML
    public void initialize() {
        update_password.setOnAction(event -> updatePassword());
        retour.setOnAction(event -> navigateBack());
    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
    }

    private void updateUI() {
        name_userconnecte.setText(userProfile.getName_u());
        if (userProfile.getImage_u() != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                img_userconnecte.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
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

    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
            Parent root = loader.load();

            dashboardAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) retour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
