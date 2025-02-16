package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceUser;
import com.esprit.services.ServiceProfile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginn {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnSignIn;

    @FXML
    public void initialize() {
        // Cette méthode n'est plus nécessaire car nous utilisons onAction dans le FXML
    }

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Pour déboguer
        System.out.println("Tentative de connexion avec : " + username);

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs!");
            return;
        }

        ServiceUser serviceUser = new ServiceUser();
        
        if (!serviceUser.verifierUsername(username)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Username n'existe pas!");
            return;
        }

        user connectedUser = serviceUser.verifierLogin(username, password);
        if (connectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Mot de passe incorrect!");
            return;
        }

        // Pour déboguer
        System.out.println("Utilisateur authentifié...");

        ServiceProfile serviceProfile = new ServiceProfile();
        profile userProfile = serviceProfile.getProfileByUserId(connectedUser.getId());
        System.out.println("tesst");
        System.out.println(userProfile);

        if (userProfile != null) {
            if (userProfile.getRole().equals("Admin")) {
                navigateToWithData("/dashboard.fxml", connectedUser, userProfile);
            } else if (userProfile.getRole().equals("User")) {
                navigateToWithData("/Accueil.fxml", connectedUser, userProfile);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Profil non trouvé!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToWithData(String fxmlPath, user connectedUser, profile userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Récupérer le contrôleur de la nouvelle vue
            Object controller = loader.getController();
            
            // Vérifier le type de contrôleur et définir les données
            if (controller instanceof dashboardController) {
                dashboardController dashCtrl = (dashboardController) controller;
                dashCtrl.initData(connectedUser, userProfile);
            } else {
                // Pour le contrôleur Accueil (à créer si ce n'est pas déjà fait)
                AccueilController accCtrl = (AccueilController) controller;
                accCtrl.initData(connectedUser, userProfile);
            }

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation!");
        }
    }
}
