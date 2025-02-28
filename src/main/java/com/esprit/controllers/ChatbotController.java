package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
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
    private VBox container;

    @FXML
    private Label label1, label2, label3;

    @FXML
    private TextArea chatAreaUser, chatArea;  // Déclaration des TextArea

    private static final String API_KEY = "AIzaSyAHy8vt_mHVPGBjVEERBKIqrXYnp1czAV4";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @FXML
    private void initialize() {
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

        // Initialisation des TextArea (visible, non éditable, etc.)
        chatAreaUser.setEditable(false);
        chatAreaUser.setWrapText(true);
        chatAreaUser.setPrefHeight(100);
        chatAreaUser.setVisible(false); // Masqué initialement

        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(100);
        chatArea.setVisible(false); // Masqué initialement



    }

    // Show chat interface when interaction is detected
    private void afficherChatInterface() {
        Platform.runLater(() -> {
            // Create new TextArea for user and chatbot messages
            TextArea newUserTextArea = new TextArea();
            TextArea newChatbotTextArea = new TextArea();

            // Set TextArea properties
            newUserTextArea.setEditable(false);
            newUserTextArea.setWrapText(true);
            newUserTextArea.setVisible(true);  // Make sure TextArea is visible

            newChatbotTextArea.setEditable(false);
            newChatbotTextArea.setWrapText(true);
            newChatbotTextArea.setVisible(true);  // Make sure TextArea is visible

            adjustTextAreaHeight(newUserTextArea);
            adjustTextAreaHeight(newChatbotTextArea);
            // Apply CSS classes to style
            applyStyles(newUserTextArea, "user-text-area");
            applyStyles(newChatbotTextArea, "chatbot-text-area");

            // Add TextArea to the VBox (chatContainer)
            container.getChildren().addAll(newUserTextArea, newChatbotTextArea);

            // Scroll to the bottom of the container to show the latest message
            scrollToBottom();
        });
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
        Platform.runLater(() -> {
            // Get the latest user message TextArea
            TextArea currentUserTextArea = (TextArea) container.getChildren().get(container.getChildren().size() - 2);
            currentUserTextArea.appendText("Vous: " + message + "\n");
            currentUserTextArea.setVisible(true);  // Ensure the user message TextArea is visible
            adjustTextAreaHeight(currentUserTextArea);
            scrollToBottom();
        });
    }

    // Method to add the chatbot's message to the chat area
    private void ajouterMessageChatbot(String message) {
        Platform.runLater(() -> {
            // Get the latest chatbot message TextArea
            TextArea currentChatbotTextArea = (TextArea) container.getChildren().get(container.getChildren().size() - 1);
            currentChatbotTextArea.appendText("Chatbot: " + message + "\n");
            currentChatbotTextArea.setVisible(true);  // Ensure the chatbot message TextArea is visible
            adjustTextAreaHeight(currentChatbotTextArea
            );
            scrollToBottom();
        });
    }

    // Method to make the conversation scroll to the bottom automatically
    private void scrollToBottom() {
        Platform.runLater(() -> {
            // Scroll to the bottom of the last two TextAreas
            TextArea currentUserTextArea = (TextArea) container.getChildren().get(container.getChildren().size() - 2);
            TextArea currentChatbotTextArea = (TextArea) container.getChildren().get(container.getChildren().size() - 1);

            currentUserTextArea.setScrollTop(Double.MAX_VALUE);
            currentChatbotTextArea.setScrollTop(Double.MAX_VALUE);
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

    private void adjustTextAreaHeight(TextArea textArea) {
        // Disable scrolling
        textArea.setWrapText(true);
        textArea.setScrollTop(0);  // Désactive la barre de défilement
        textArea.setPrefHeight(0);  // Réinitialise la hauteur avant d'ajuster

        // Calculer le nombre de lignes de texte
        int lines = textArea.getText().split("\n").length;

        // Ajuster la hauteur selon le nombre de lignes (multiplier selon la taille de la ligne et le padding)
        int preferredHeight = lines * 20; // Ajustez ce facteur selon votre police et espacement

        // Assurer une hauteur minimale
        preferredHeight = Math.max(preferredHeight, 50); // Hauteur minimale de 50px

        // Appliquer la nouvelle hauteur
        textArea.setPrefHeight(preferredHeight);
    }

    private void applyStyles(TextArea textArea, String styleClass) {
        textArea.getStyleClass().add(styleClass);  // Applique la classe CSS
    }
}
