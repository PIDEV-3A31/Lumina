package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SignUp {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtTelephone;
    @FXML
    private ComboBox<String> cbRole;
    @FXML
    private CheckBox checkbox;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button affichMdp1;
    @FXML
    private Button affichMdp2;
    @FXML
    private ImageView eyeIcon1;
    @FXML
    private ImageView eyeIcon2;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @FXML
    public void initialize() {
        btnSignUp.setOnAction(this::SignUp);
        affichMdp1.setOnAction(event -> togglePasswordVisibility(txtPassword, affichMdp1, eyeIcon1, isPasswordVisible));
        affichMdp2.setOnAction(event -> togglePasswordVisibility(txtConfirmPassword, affichMdp2, eyeIcon2, isConfirmPasswordVisible));
    }

    private void SignUp(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        try {
            // Créer et sauvegarder l'utilisateur
            user newUser = new user(txtUsername.getText(), txtPassword.getText());
            ServiceUser su = new ServiceUser();
            int userId = su.ajouterAvecId(newUser);
            
            if (userId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create user!");
                return;
            }

            // Créer et sauvegarder le profil avec l'ID de l'utilisateur
            profile newProfile = new profile(
                userId,  // Utiliser l'ID de l'utilisateur créé
                txtNom.getText(),
                txtEmail.getText(),
                Integer.parseInt(txtTelephone.getText()),
                cbRole.getValue()
            );
            
            ServiceProfile sp = new ServiceProfile();
            sp.ajouter(newProfile);

            if (cbRole.getValue().equals("Admin")) {
                navigateToAdminDashboard();
            }
            if (cbRole.getValue().equals("Student")){
                navigateToAccueilController();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error creating account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        // Vérification des champs vides
        if (txtUsername.getText().isEmpty() || txtEmail.getText().isEmpty() || 
            txtPassword.getText().isEmpty() || txtNom.getText().isEmpty() || 
            txtTelephone.getText().isEmpty() || cbRole.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            return false;
        }

        // Vérification de la longueur du username
        if (txtUsername.getText().length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username must not exceed 20 characters!");
            return false;
        }

        // Vérification de la longueur du nom
        if (txtNom.getText().length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Error", "Name must not exceed 25 characters!");
            return false;
        }

        // Vérification du numéro de téléphone (8 chiffres)
        if (!txtTelephone.getText().matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be exactly 8 digits!");
            return false;
        }

        // Validation de l'email
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address!");
            return false;
        }

        if (!checkbox.isSelected()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please accept the terms and conditions!");
            return false;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load admin dashboard!");
        }
    }
    private void navigateToAccueilController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccueilController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load admin dashboard!");
        }
    }

    private void togglePasswordVisibility(PasswordField passwordField, Button button, ImageView eyeIcon, boolean isVisible) {
        if (!isVisible) {
            String password = passwordField.getText();
            passwordField.setPromptText(passwordField.getText());
            passwordField.clear();
            passwordField.setStyle("-fx-text-inner-color: black;");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye.png").toExternalForm()));
        } else {
            String password = passwordField.getPromptText();
            passwordField.setText(password);
            passwordField.setPromptText("");
            passwordField.setStyle("");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye-slash.png").toExternalForm()));
        }

        if (passwordField == txtPassword) {
            isPasswordVisible = !isPasswordVisible;
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        }
    }
}
