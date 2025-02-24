package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class UploadDocumentKycController {
    private static final String API_URL = "https://api.eu.onfido.com/v3.6";
    private static final String API_KEY = "api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB";

    @FXML private TextField applicantIdField;
    @FXML private ComboBox<String> documentTypeComboBox;
    @FXML private Label filePathLabel;
    @FXML private Label statusLabel;

    private File selectedFile;
    private final OkHttpClient client = new OkHttpClient();

    @FXML
    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images & PDF", "*.jpg", "*.jpeg", "*.png", "*.pdf"));

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePathLabel.setText("Fichier sélectionné: " + selectedFile.getName());
        }
    }

    @FXML
    private void uploadDocument() {
        String applicantId = applicantIdField.getText();
        String documentType = documentTypeComboBox.getValue();

        if (applicantId.isEmpty() || documentType == null || selectedFile == null) {
            statusLabel.setText("Veuillez remplir tous les champs et sélectionner un fichier !");
            return;
        }

        try {
            String documentId = uploadDocumentToOnfido(applicantId,documentType, selectedFile);
            if (documentId != null) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Document uploadé avec succès ! ID: " + documentId);
            } else {
                statusLabel.setText("Erreur lors de l'upload !");
            }
        } catch (IOException e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    public static String uploadDocumentToOnfido(String applicantId, String documentType, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("applicant_id", applicantId)
                .addFormDataPart("type", documentType)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.get("image/png"))) // Assure-toi d'utiliser le bon type
                .build();

        Request request = new Request.Builder()
                .url(API_URL + "/documents")
                .header("Authorization", "Token token=" + API_KEY) // Garde un seul header Authorization
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erreur API: " + response.code() + " - " + response.message());
                return null;
            }

            String responseString = response.body() != null ? response.body().string() : null;
            System.out.println("Réponse API: " + responseString);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            return jsonResponse.has("id") ? jsonResponse.get("id").asText() : null;
        }
    }

}
