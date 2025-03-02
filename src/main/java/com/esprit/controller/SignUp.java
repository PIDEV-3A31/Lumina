package com.esprit.controller;

import com.esprit.models.Point;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import com.esprit.utils.PointHistory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    @FXML
    private Button uploadImg;
    @FXML
    private ImageView image;
    @FXML
    private Label toLogin;
    @FXML
    private Label Alert_conditions;
    @FXML
    private Label Alert_confirmpassword;
    @FXML
    private Label Alert_email;
    @FXML
    private Label Alert_name;
    @FXML
    private Label Alert_password;
    @FXML
    private Label Alert_phone;
    @FXML
    private Label Alert_username;
    @FXML
    private Tooltip ttt;
    @FXML
    private Button tt;
    @FXML
    private TextField txtCodeParrainage;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        btnSignUp.setOnAction(this::SignUp);
        uploadImg.setOnAction(event -> ImageUpload());
        affichMdp1.setOnAction(event -> togglePasswordVisibility(txtPassword, affichMdp1, eyeIcon1, isPasswordVisible));
        affichMdp2.setOnAction(event -> togglePasswordVisibility(txtConfirmPassword, affichMdp2, eyeIcon2, isConfirmPasswordVisible));
        toLogin.setOnMouseClicked(event -> navigateToLogin());
        toLogin.setStyle("-fx-cursor: hand;");
        tt.setStyle("-fx-font: normal bold 12px Langdon; "
                + "-fx-base: #9E9B9A; "
                + "-fx-text-fill: gris;");
        ttt.setText("The password must be between 3 and 20 characters long and contain at least one uppercase letter.");
        tt.setTooltip(ttt);
    }

    private void ImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Copier l'image vers le dossier uploads
                Path sourcePath = selectedFile.toPath();
                Path targetPath = Paths.get("src/main/resources/uploads/" + selectedFile.getName());
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour l'affichage et sauvegarder le chemin
                selectedImagePath = "uploads/" + selectedFile.getName();
                Image img = new Image(targetPath.toUri().toString());
                image.setImage(img);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }

    private void SignUp(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        try {
            user newUser = new user(txtUsername.getText(), txtPassword.getText());
            ServiceUser su = new ServiceUser();
            
            // Créer d'abord l'utilisateur pour avoir son ID
            int userId = su.ajouterAvecId(newUser);
            if (userId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create user!");
                return;
            }
            
            // Mettre à jour l'ID de l'utilisateur
            newUser.setId(userId);
            
            // Gérer le code de parrainage
            String referralCode = txtCodeParrainage.getText();
            if (!referralCode.isEmpty()) {
                user referrer = su.getUserByReferralCode(referralCode);
                if (referrer != null) {
                    // Ajouter les points au parrain
                    su.addPoints(referrer.getId(), 50, "Parrainage d'un nouveau membre");
                    
                    // Ajouter les points au filleul
                    su.addPoints(userId, 20, "Inscription avec code de parrainage");
                    
                    // Créer les objets Point avec pointId initialisé à 0 (sera auto-incrémenté lors de la sauvegarde)
                    Point referrerPoints = new Point(
                        referrer.getId(),
                        50,
                        "Parrainage de " + newUser.getUsername()
                    );
                    Point newUserPoints = new Point(
                        userId,
                        20,
                        "Inscription avec le code de " + referrer.getUsername()
                    );
                    
                    // Sauvegarder les points dans l'historique
                    PointHistory.savePoint(referrerPoints);
                    PointHistory.savePoint(newUserPoints);
                }
            }

            // Créer le profil
            profile newProfile = new profile(
                userId,
                txtNom.getText(),
                txtEmail.getText(),
                Integer.parseInt(txtTelephone.getText()),
                "User",
                selectedImagePath
            );
            
            ServiceProfile sp = new ServiceProfile();
            sp.ajouter(newProfile);

            // Récupérer le profil créé pour la navigation
            profile userProfile = sp.getProfileByUserId(userId);
            user connectedUser = su.getUserById(userId);

            // Navigation
            navigateToUserHome(connectedUser, userProfile);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error creating account: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        ServiceUser serviceUser = new ServiceUser();
        ServiceProfile serviceProfile = new ServiceProfile();
        boolean isvalid = true;

        Alert_username.setText("");
        Alert_email.setText("");
        Alert_password.setText("");
        Alert_name.setText("");
        Alert_phone.setText("");
        Alert_confirmpassword.setText("");
        Alert_conditions.setText("");
        // Vérification de l'unicité du username
        if (!serviceUser.isUsernameUnique(txtUsername.getText(), null) || txtUsername.getText().isEmpty()) {
            if(txtUsername.getText().isEmpty())
                Alert_username.setText("Username is required!");
            else
                Alert_username.setText("Username already exists!");
            isvalid = false;
        }

        // Vérification de l'unicité de l'email
        if (!serviceProfile.isEmailUnique(txtEmail.getText(), null) || txtEmail.getText().isEmpty()) {
            if(txtEmail.getText().isEmpty())
                Alert_email.setText("Email is required!");
            else
                Alert_email.setText("Email already exists!");
            isvalid = false;
        }


        // Vérification de la longueur du username
        if (txtUsername.getText().length() > 20 || txtUsername.getText().length() < 2) {
            Alert_username.setText("Username must be between 2 and 25 characters!");
            isvalid = false;
        }


        // Vérification de la longueur du nom
        if (txtNom.getText().length() > 25 || txtNom.getText().length() < 2 || txtNom.getText().isEmpty()) {
            if(txtNom.getText().isEmpty())
                Alert_name.setText("Nom is required!");
            else
                Alert_name.setText("Name must be between 2 and 25 characters!");
            isvalid = false;
        }

        // numero de tel doit commencer par 9,4,5,2 et 8chiffres
        if (!txtTelephone.getText().matches("^[5294]\\d{7}$") || txtTelephone.getText().isEmpty()) {
            if(txtTelephone.getText().isEmpty())
                Alert_phone.setText("Telephone is required!");
            else
                Alert_phone.setText("Phone number must start with 5, 2, 9, or 4 and have exactly 8 digits!");
            isvalid = false;
        }

        // Validation de l'email
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$") || txtEmail.getText().isEmpty()) {
            if(txtEmail.getText().isEmpty())
                Alert_email.setText("Email is required!");
            else
                Alert_email.setText("Please enter a valid email address!");
            isvalid = false;
        }

        if (!checkbox.isSelected()) {
            Alert_conditions.setText("Please accept the terms and conditions!");
            isvalid = false;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText()) || txtConfirmPassword.getText().isEmpty()) {
            if(txtConfirmPassword.getText().isEmpty())
                Alert_confirmpassword.setText("Please confirm your password!");
            else
                Alert_confirmpassword.setText("Passwords do not match!");
            isvalid = false;
        }
        if (!serviceUser.isValidPassword(txtPassword.getText()) || txtPassword.getText().isEmpty()) {
            if(txtPassword.getText().isEmpty())
                Alert_password.setText("Password is required!");
            else
                Alert_password.setText("The password must be between 3 and 20 characters long and contain at least one uppercase letter!");
            isvalid = false;
        }

        return isvalid;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToDashboard(user user, profile userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            dashboardController controller = loader.getController();
            controller.initData(user, userProfile);

            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation error!");
        }
    }

    private void navigateToUserHome(user user, profile userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_home.fxml"));
            Parent root = loader.load();

            frontHome controller = loader.getController();
            controller.initData(user, userProfile);

            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation error!");
        }
    }

    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/loginn.fxml"));
            Stage stage = (Stage) toLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation error!");
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

