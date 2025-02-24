package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CheckVerificationController {
    private static final String API_URL = "https://api.eu.onfido.com/v3.6";
    private static final String API_KEY = "api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB";

    @FXML private TextField applicantIdField;
    @FXML private TextField documentIdField;
    @FXML private Label statusLabel;

    private final OkHttpClient client = new OkHttpClient();

    // Vérifier le statut de la vérification
    @FXML
    private void checkVerificationStatus() {
        String documentId = documentIdField.getText();

        if (documentId.isEmpty()) {
            statusLabel.setText("Veuillez entrer un document ID pour vérifier le statut !");
            return;
        }

        try {
            String checkId = createVerificationCheck(applicantIdField.getText(), documentId);
            if (checkId != null) {
                checkVerificationResult(checkId);
            } else {
                statusLabel.setText("Erreur lors de la création du check !");
            }
        } catch (IOException e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    // Créer le check de vérification pour un document
    public static String createVerificationCheck(String applicantId, String documentId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("applicant_id", applicantId)
                .addFormDataPart("document_id", documentId)
                .addFormDataPart("type", "document")  // Le type peut être 'document' ou un autre type selon l'API Onfido
                .build();

        // Créer la requête avec l'URL appropriée et l'API key dans l'en-tête Authorization
        Request request = new Request.Builder()
                .url(API_URL + "/checks")  // URL pour créer le check
                .header("Authorization", "Token token=" + API_KEY)  // En-tête d'authentification
                .post(requestBody)  // Méthode POST avec le corps de la requête
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erreur API: " + response.code() + " - " + response.message());
                return null;
            }

            String responseString = response.body() != null ? response.body().string() : null;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            return jsonResponse.has("id") ? jsonResponse.get("id").asText() : null;
        }
    }

    // Vérifier le statut du check
    private void checkVerificationResult(String checkId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL + "/checks/" + checkId)
                .header("Authorization", "Token token=" + API_KEY)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erreur API: " + response.code() + " - " + response.message());
                statusLabel.setText("Erreur lors de la récupération du statut !");
                return;
            }

            String responseString = response.body() != null ? response.body().string() : null;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            String status = jsonResponse.has("status") ? jsonResponse.get("status").asText() : "Inconnu";
            if ("completed".equals(status)) {
                statusLabel.setText("Vérification terminée avec succès !");
            } else if ("pending".equals(status)) {
                statusLabel.setText("Vérification en attente...");
            } else {
                statusLabel.setText("Erreur lors de la vérification !");
            }
        }
    }
}
