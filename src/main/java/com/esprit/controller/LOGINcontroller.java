package com.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LOGINcontroller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    // Méthode pour gérer le login
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Exemple de vérification de connexion (à remplacer par une logique réelle)
        if (username.equals("admin") && password.equals("password")) {
            // Connexion réussie
            System.out.println("Connexion réussie !");
            // Tu peux ici charger une autre scène ou une page après la connexion réussie.
        } else {
            // Connexion échouée
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Identifiants invalides");
            alert.setContentText("Le nom d'utilisateur ou le mot de passe est incorrect.");
            alert.showAndWait();
        }
    }
}
