package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class frontUpdateProf {
    private user connectedUser;
    private profile userProfile;
    private String selectedImagePath = null;

    @FXML
    private TextField modifusername;
    @FXML
    private TextField modifemail;
    @FXML
    private TextField modifname;
    @FXML
    private TextField modifphone;
    @FXML
    private Button Save;
    @FXML
    private ImageView image;
    @FXML
    private Button uploadImg;
    @FXML
    private Button retour;
    @FXML
    private ImageView img_current_user;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView deconnexion;
    @FXML
    private ImageView to_home;

    @FXML
    public void initialize() {
        Save.setOnAction(event -> Update());
        uploadImg.setOnAction(event -> ImageUpload());
        retour.setOnAction(event -> navigateBack());
        to_home.setOnMouseClicked(event -> navigateToHome());
        deconnexion.setOnMouseClicked(event -> logout());
        name_current_user.setOnMouseClicked(event -> navigateToProfile());

        // Style du curseur
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
        modifusername.setText(connectedUser.getUsername());
        modifname.setText(userProfile.getName_u());
        modifemail.setText(userProfile.getEmail_u());
        modifphone.setText(String.valueOf(userProfile.getPhone_u()));

        loadImage(userProfile.getImage_u(), img_current_user);
        loadImage(userProfile.getImage_u(), image);
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

    private void Update() {
        try {
            // Validation des champs
            if (modifusername.getText().isEmpty() || modifemail.getText().isEmpty() || 
                modifname.getText().isEmpty() || modifphone.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires!");
                return;
            }

            // Mise à jour du user
            connectedUser.setUsername(modifusername.getText());
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.modifer(connectedUser, connectedUser.getId());

            // Mise à jour du profile
            userProfile.setName_u(modifname.getText());
            userProfile.setEmail_u(modifemail.getText());
            userProfile.setPhone_u(Integer.parseInt(modifphone.getText()));
            
            if (selectedImagePath != null) {
                userProfile.setImage_u(selectedImagePath);
                // Mettre à jour immédiatement toutes les ImageView
                updateAllImages(selectedImagePath);
            }

            ServiceProfile serviceProfile = new ServiceProfile();
            serviceProfile.modifer(userProfile, userProfile.getId_profile());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès!");
            navigateBack();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    private void updateAllImages(String imagePath) {
        // Mettre à jour toutes les ImageView de la vue actuelle
        loadImage(imagePath, img_current_user);
        loadImage(imagePath, image);
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

    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAffichProf.fxml"));
            Parent root = loader.load();

            frontAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) retour.getScene().getWindow();
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
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
