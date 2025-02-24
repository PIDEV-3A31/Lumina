package com.esprit.controllers;

import com.esprit.services.OnfidoService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class OnfidoController {
    private final OnfidoService onfidoService = new OnfidoService();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextArea resultArea;

    @FXML
    private void handleCreateApplicant() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        try {
            String response = onfidoService.createApplicant(firstName, lastName);
            resultArea.setText(response);
        } catch (IOException e) {
            resultArea.setText("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }
}
