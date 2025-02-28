package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatbotController {

    @FXML
    private VBox container;
    @FXML
    private Button button1, button2, button3, sendButton;
    @FXML
    private TextField userInput;

    @FXML
    private TextArea chatArea; // La zone de texte sera ajoutée plus tard

    private static final String API_KEY = "AIzaSyAHy8vt_mHVPGBjVEERBKIqrXYnp1czAV4";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @FXML
    private void initialize() {
        // Associer chaque bouton à une action
        button1.setOnAction(event -> handlePromptChoice("Bonjour"));
        button2.setOnAction(event -> handlePromptChoice("Quel temps fait-il ?"));
        button3.setOnAction(event -> handlePromptChoice("Raconte-moi une blague."));

        sendButton.setOnAction(event -> handleUserInput());
    }

    // ⚡ Méthode appelée lorsqu'un bouton est cliqué
    @FXML
    private void handlePromptChoice(String prompt) {
        afficherChatInterface(); // Afficher la zone de chat
        chatArea.appendText("Vous: " + prompt + "\n");
        new Thread(() -> {
            String response = getChatbotResponse(prompt);
            Platform.runLater(() -> chatArea.appendText("Chatbot: " + response + "\n"));
        }).start();

        // Cacher les boutons après un clic
        hideButtons();
    }

    // ⚡ Méthode appelée lorsque l'utilisateur envoie une question
    @FXML
    private void handleUserInput() {
        String message = userInput.getText().trim();
        if (!message.isEmpty()) {
            afficherChatInterface(); // Afficher la zone de chat
            chatArea.appendText("Vous: " + message + "\n");
            userInput.clear();

            new Thread(() -> {
                String response = getChatbotResponse(message);
                Platform.runLater(() -> chatArea.appendText("Chatbot: " + response + "\n"));
            }).start();

            // Cacher les boutons après un envoi de texte
            hideButtons();
        }
    }

    // ⚡ Remplace les boutons par la zone de chat
    private void afficherChatInterface() {
        if (container == null) {
            System.out.println("Erreur : container est null !");
            return;
        }

        if (container.getChildren().size() == 3) { // Si la zone de chat n'est pas déjà affichée
            chatArea = new TextArea(); // Crée une nouvelle TextArea
            chatArea.setEditable(false);
            chatArea.setPrefHeight(250);

            Platform.runLater(() -> {
                container.getChildren().clear(); // Supprimer les boutons
                container.getChildren().addAll(chatArea, userInput, sendButton);
            });
        }
    }

    // ⚡ Envoie une requête à l'API du chatbot
    private String getChatbotResponse(String message) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
                os.flush();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return parseResponse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la connexion à l'API";
        }
    }

    // ⚡ Analyse la réponse JSON du chatbot
    private String parseResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            e.printStackTrace();
            return "Réponse invalide";
        }
    }

    // ⚡ Cache les boutons
    private void hideButtons() {
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
    }
}
