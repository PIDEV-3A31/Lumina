package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceUser;
import com.esprit.services.ServiceProfile;
import com.esprit.utils.Mailer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.esprit.utils.NavigationHistory;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

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
    @FXML private Label ResetPassword;

    private boolean isPasswordVisible = false;
    @FXML
    public void initialize() {
        affich_mdp.setOnAction(event -> togglePasswordVisibility(txtPassword, affich_mdp, eye_icon, isPasswordVisible));
        toSignUp.setOnMouseClicked(event -> toSignUp());
        toSignUp.setStyle("-fx-cursor: hand;");
        ResetPassword.setOnMouseClicked(event -> {
            try {
                ResetPassword();
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void ResetPassword() throws AddressException {
        // Réinitialiser les messages d'erreur
        Alert_username.setText("");
        Alert_password.setText("");

        String username = txtUsername.getText();
        
        if (username.isEmpty()) {
            Alert_username.setText("Please enter your username first!");
            return;
        }

        ServiceUser serviceUser = new ServiceUser();
        ServiceProfile serviceProfile = new ServiceProfile();
        user user = serviceUser.getUserByUsername(username);

        if (user == null) {
            Alert_username.setText("Username does not exist!");
            return;
        }

        String email_user = serviceProfile.getEmailByUserId(user.getId());
        if (email_user == null || email_user.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No email address found for this user!");
            return;
        }

        try {
            // Générer et envoyer le code
            String generatedCode = generateRandomCode();
            new Mailer().sendMailAsync(email_user, "Password Reset Code", 
                "Dear " + username + ",\n\n" +
                "You have requested to reset your password.\n" +
                "Your verification code is: " + generatedCode + "\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Best regards,\nYour Application Team");

            // Dialogue de vérification du code
            TextInputDialog codeDialog = new TextInputDialog("");
            codeDialog.setTitle("Verify Code");
            codeDialog.setHeaderText("Check your email");
            codeDialog.setContentText("Enter the verification code sent to " + maskEmail(email_user) + ":");

            Optional<String> codeResult = codeDialog.showAndWait();

            if (codeResult.isPresent()) {
                if (codeResult.get().equals(generatedCode)) {
                    // Code correct, demander le nouveau mot de passe
                    Dialog<String> passwordDialog = new Dialog<>();
                    passwordDialog.setTitle("New Password");
                    passwordDialog.setHeaderText("Enter your new password");

                    // Créer les champs de mot de passe
                    PasswordField newPassword = new PasswordField();
                    PasswordField confirmPassword = new PasswordField();
                    newPassword.setPromptText("New password");
                    confirmPassword.setPromptText("Confirm password");

                    // Créer la mise en page
                    VBox content = new VBox(10);
                    content.getChildren().addAll(
                        new Label("New password:"), newPassword,
                        new Label("Confirm password:"), confirmPassword
                    );
                    passwordDialog.getDialogPane().setContent(content);

                    // Ajouter les boutons
                    ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                    passwordDialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

                    // Activer/désactiver le bouton de confirmation selon la validation
                    Node confirmButtonNode = passwordDialog.getDialogPane().lookupButton(confirmButton);
                    confirmButtonNode.setDisable(true);

                    // Validation en temps réel
                    ChangeListener<String> passwordListener = (observable, oldValue, newValue) -> {
                        boolean isValid = !newPassword.getText().isEmpty() && 
                                        !confirmPassword.getText().isEmpty() &&
                                        newPassword.getText().equals(confirmPassword.getText()) &&
                                        serviceUser.isValidPassword(newPassword.getText());
                        confirmButtonNode.setDisable(!isValid);
                    };

                    newPassword.textProperty().addListener(passwordListener);
                    confirmPassword.textProperty().addListener(passwordListener);

                    // Convertir le résultat
                    passwordDialog.setResultConverter(dialogButton -> {
                        if (dialogButton == confirmButton) {
                            return newPassword.getText();
                        }
                        return null;
                    });

                    Optional<String> newPasswordResult = passwordDialog.showAndWait();
                    if (newPasswordResult.isPresent()) {
                        // Mettre à jour le mot de passe
                        user.setPassword(newPasswordResult.get());
                        serviceUser.modifer(user, user.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Your password has been successfully reset!");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid verification code!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "An error occurred during password reset: " + e.getMessage());
        }
    }

    private String generateRandomCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.valueOf(code);
    }

    private String maskEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];
        
        if (name.length() <= 2) {
            return name + "@" + domain;
        }
        
        return name.substring(0, 2) + "***" + "@" + domain;
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
            ServiceProfile serviceProfile = new ServiceProfile();
            profile userProfile = serviceProfile.getProfileByUserId(connectedUser.getId());

            if (userProfile != null) {
                String role = userProfile.getRole();
                if ("Admin".equals(role)) {
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

            // Récupérer le contrôleur et initialiser les données
            dashboardController controller = loader.getController();
            controller.initData(user, userProfile);

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
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

    public void toResetPassword() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ResetPassword.fxml"));
            Stage stage = (Stage) toSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }
}
