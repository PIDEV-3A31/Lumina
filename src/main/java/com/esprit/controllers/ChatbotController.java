package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatbotController {


    @FXML
    private Button button1, button2, button3, sendButton;
    @FXML
    private TextField userInput;
    @FXML
    private TextArea chatArea;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label label1, label2, label3;

    private static final String API_KEY = "AIzaSyAHy8vt_mHVPGBjVEERBKIqrXYnp1czAV4";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @FXML
    private void initialize() {
        label1.setText("Bonjour");
        label2.setText("Quel temps fait-il ?");
        label3.setText("Raconte-moi une blague.");

        button1.setOnAction(event -> handlePromptChoice(label1.getText()));
        button2.setOnAction(event -> handlePromptChoice(label2.getText()));
        button3.setOnAction(event -> handlePromptChoice(label3.getText()));

        chatArea.setVisible(false);

    }

    @FXML
    private void handleUserInput() {
        String message = userInput.getText().trim();
        if (!message.isEmpty()) {
            ajouterMessageUtilisateur(message);
            userInput.clear();
            envoyerMessageAuChatbot(message);
            hideButtons();
        }
    }

    @FXML
    private void handlePromptChoice(String prompt) {
        chatArea.setVisible(true);
        ajouterMessageUtilisateur(prompt);
        envoyerMessageAuChatbot(prompt);
        hideButtons();
    }

    private void ajouterMessageUtilisateur(String message) {
        Platform.runLater(() -> {
            chatArea.appendText("Vous: " + message + "\n");

        });
    }

    private void ajouterMessageChatbot(String message) {
        Platform.runLater(() -> {
            chatArea.appendText("Chatbot: " + message + "\n");
        });
    }

    private void envoyerMessageAuChatbot(String message) {
        new Thread(() -> {
            String response = getChatbotResponse(message);
            Platform.runLater(() -> ajouterMessageChatbot(response));
        }).start();
    }

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

    private void hideButtons() {
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
    }
}
