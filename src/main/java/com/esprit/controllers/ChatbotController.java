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
    private TextArea chatArea, chatAreaUser;

    @FXML
    private Label label1, label2, label3;

    private static final String API_KEY = "AIzaSyAHy8vt_mHVPGBjVEERBKIqrXYnp1czAV4";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @FXML
    private void initialize() {
        // Hide chat areas initially
        chatArea.setVisible(false);
        chatAreaUser.setVisible(false);

        // Configure buttons
        button1.setOnAction(event -> handlePromptChoice("Bonjour"));
        button2.setOnAction(event -> handlePromptChoice("Quel temps fait-il ?"));
        button3.setOnAction(event -> handlePromptChoice("Raconte-moi une blague."));

        label1.setText("Bonjour");
        label2.setText("Quel temps fait-il ?");
        label3.setText("Raconte-moi une blague.");

        label1.setWrapText(true);
        label2.setWrapText(true);
        label3.setWrapText(true);

        sendButton.setOnAction(event -> handleUserInput());
    }

    // Show chat interface when interaction is detected
    private void afficherChatInterface() {
        if (!chatArea.isVisible() || !chatAreaUser.isVisible()) {
            Platform.runLater(() -> {
                chatArea.setVisible(true);
                chatAreaUser.setVisible(true);
            });
        }
    }

    // Method to handle the prompt choice from the buttons
    @FXML
    private void handlePromptChoice(String prompt) {
        afficherChatInterface();
        ajouterMessageUtilisateur(prompt);

        new Thread(() -> {
            String response = getChatbotResponse(prompt);
            Platform.runLater(() -> ajouterMessageChatbot(response));
        }).start();

        hideButtons();
    }

    // Method to handle user input when the send button is clicked
    @FXML
    private void handleUserInput() {
        String message = userInput.getText().trim();
        if (!message.isEmpty()) {
            afficherChatInterface();
            ajouterMessageUtilisateur(message);
            userInput.clear();

            new Thread(() -> {
                String response = getChatbotResponse(message);
                Platform.runLater(() -> ajouterMessageChatbot(response));
            }).start();

            hideButtons();
        }
    }

    // Method to add the user's message to the chat area
    private void ajouterMessageUtilisateur(String message) {
        chatAreaUser.appendText("Vous: " + message + "\n");
        scrollToBottom();
    }

    // Method to add the chatbot's message to the chat area
    private void ajouterMessageChatbot(String message) {
        chatArea.appendText("Chatbot: " + message + "\n");
        scrollToBottom();
    }

    // Method to make the conversation scroll to the bottom automatically
    private void scrollToBottom() {
        Platform.runLater(() -> {
            chatArea.setScrollTop(Double.MAX_VALUE);
            chatAreaUser.setScrollTop(Double.MAX_VALUE);
        });
    }

    // Method to send a request to the chatbot API
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

    // Method to parse the JSON response and remove unwanted characters (e.g., asterisks)
    private String parseResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String text = jsonObject.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return text.replace("*", ""); // Remove asterisks from the response
        } catch (Exception e) {
            e.printStackTrace();
            return "Réponse invalide";
        }
    }

    // Method to hide buttons after a choice is made or message is sent
    private void hideButtons() {
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
    }
}
