package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceUser;
import com.esprit.services.ServiceProfile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.esprit.utils.NavigationHistory;

public class loginn {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button affich_mdp;
    @FXML
    private ImageView eye_icon;
    @FXML
    private Label toSignUp;
    @FXML private Label Alert_username;
    @FXML private Label Alert_password;

    private boolean isPasswordVisible = false;
    @FXML
    public void initialize() {
        affich_mdp.setOnAction(event -> togglePasswordVisibility(txtPassword, affich_mdp, eye_icon, isPasswordVisible));
        toSignUp.setOnMouseClicked(event -> toSignUp());
        toSignUp.setStyle("-fx-cursor: hand;");
    }

    @FXML
    public void Login() {
        // Réinitialiser les messages d'erreur
        Alert_username.setText("");
        Alert_password.setText("");
        boolean isvalid = true;
        
        String username = txtUsername.getText();
        String password = txtPassword.getText();


        ServiceUser serviceUser = new ServiceUser();
        
        if (!serviceUser.verifierUsername(username) || username.isEmpty()) {
            if(username.isEmpty())
                Alert_username.setText("Please enter your username!");
            else
                Alert_username.setText("Username does not exist!");
            isvalid = false;
        }

        user connectedUser = serviceUser.verifierLogin(username, password);
        if (connectedUser == null || password.isEmpty()) {
            if(password.isEmpty())
                Alert_password.setText("Please enter your password!");
            else
                Alert_password.setText("Incorrect password!");
            isvalid = false;
        }

        if (isvalid) {

            // Pour déboguer
            System.out.println("Utilisateur authentifié...");

            ServiceProfile serviceProfile = new ServiceProfile();
            profile userProfile = serviceProfile.getProfileByUserId(connectedUser.getId());
            System.out.println("tesst");
            System.out.println(userProfile);

            if (userProfile != null) {
                String role = userProfile.getRole();
                if (role.equals("Admin")) {
                    navigateToDashboard(connectedUser, userProfile);
                } else {
                    navigateToUserHome(connectedUser, userProfile);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Profil non trouvé!");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToDashboard(user user, profile userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la nouvelle vue
            Object controller = loader.getController();
            
            // Vérifier le type de contrôleur et définir les données
            if (controller instanceof dashboardController) {
                dashboardController dashCtrl = (dashboardController) controller;
                dashCtrl.initData(user, userProfile);
            } else {
                // Pour le contrôleur Accueil (à créer si ce n'est pas déjà fait)
                AccueilController accCtrl = (AccueilController) controller;
                accCtrl.initData(user, userProfile);
            }

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation!");
        }
    }

    private void navigateToUserHome(user user, profile userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_home.fxml"));
            Parent root = loader.load();

            frontHome controller = loader.getController();
            controller.initData(user, userProfile);

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    public static void logout(Stage currentStage) {
        try {
            NavigationHistory.clearHistory();
            FXMLLoader loader = new FXMLLoader(loginn.class.getResource("/loginn.fxml"));
            Parent root = loader.load();
            Stage stage = currentStage != null ? currentStage : new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (passwordField == txtPassword) {
            isPasswordVisible = !isPasswordVisible;}
    }
    public void toSignUp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
            Stage stage = (Stage) toSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }
}
