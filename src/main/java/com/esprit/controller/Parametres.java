package com.esprit.controller;

import com.esprit.models.Point;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceUser;
import com.esprit.utils.PointHistory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.List;
import java.util.Objects;

public class Parametres {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;
    @FXML
    private ToggleButton double_authentificator;
    @FXML
    private ImageView to_home;
    @FXML
    private ImageView deconnexion;
    @FXML
    private Button Generate_code;
    @FXML
    private Label code_genere;
    @FXML
    private Label points;
    @FXML
    private Button viewPointHistory;

    @FXML
    public void initialize() {
        to_home.setOnMouseClicked(event -> navigateToHome());
        deconnexion.setOnMouseClicked(event -> logout());

        // Configuration du ToggleButton
        double_authentificator.setOnAction(event -> handle2FAToggle());

        // Style du curseur
        to_home.setStyle("-fx-cursor: hand;");
        deconnexion.setStyle("-fx-cursor: hand;");
        Generate_code.setOnAction(event -> generateReferralCode());
        viewPointHistory.setOnAction(event -> showPointHistory());
    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
        updatePointsDisplay();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        loadImage(userProfile.getImage_u(), img_current_user);
        
        // Initialiser l'état du ToggleButton
        double_authentificator.setSelected(connectedUser.isIs_2fa_enabled());
    }

    private void handle2FAToggle() {
        if (double_authentificator.isSelected()) {
            // Activer 2FA
            loginn loginController = new loginn();
            loginController.setup2FA(connectedUser.getId(), userProfile.getEmail_u());
        } else {
            // Désactiver 2FA
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.disable2FA(connectedUser.getId());
            connectedUser.setIs_2fa_enabled(false);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Two-factor authentication has been disabled.");
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
    private void generateReferralCode() {
        System.out.println(connectedUser.getUsername());
        if (connectedUser == null) {
            System.out.println("Erreur : aucun utilisateur connecte.");
            return;
        }

        ServiceUser serviceUser = new ServiceUser();
        serviceUser.generateReferralCode(connectedUser.getId());

        // Récupérer l'utilisateur mis à jour
        user updatedUser = serviceUser.getUserById(connectedUser.getId());
        if (updatedUser != null && updatedUser.getCode_parrainage() != null) {
            code_genere.setText(updatedUser.getCode_parrainage());
        } else {
            System.out.println("Erreur : impossible de récupérer le code de parrainage.");
            code_genere.setText("Aucun code disponible");
        }
    }


    private void updatePointsDisplay() {
        //System.out.println(connectedUser.getUsername());
        if (connectedUser == null) {
            System.out.println("Erreur : aucun utilisateur connecté.");
            return;
        }

        ServiceUser serviceUser = new ServiceUser();
        user updatedUser = serviceUser.getUserById(connectedUser.getId());

        if (updatedUser != null) {
            points.setText(String.valueOf(updatedUser.getPoints()));
        } else {
            System.out.println("Erreur : impossible de récupérer les points.");
            points.setText("0"); // ou une valeur par défaut
        }
    }

    private void showPointHistory() {
        List<Point> history = PointHistory.loadPointHistory();
        StringBuilder content = new StringBuilder();
        content.append("Historique des points :\n\n");
        
        for (Point point : history) {
            content.append(String.format("Date: %s\n", point.getDate()));
            content.append(String.format("Action: %s\n", point.getAction()));
            content.append(String.format("Points: %d\n", point.getPoints()));
            content.append("------------------------\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historique des Points");
        alert.setHeaderText(null);
        alert.setContentText(content.toString());
        
        // Pour permettre de copier le texte
        TextArea textArea = new TextArea(content.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

}
