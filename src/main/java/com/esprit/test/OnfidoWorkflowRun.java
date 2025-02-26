package com.esprit.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

public class OnfidoWorkflowRun {

    private static final String API_URL = "https://api.eu.onfido.com/v3.6/";
    private static final String API_KEY = "Token token=api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB"; // Remplace par ton API Key
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void createWorkflowRun(String applicantId, String workflowId) throws IOException, InterruptedException {
        // Construire le JSON avec les champs requis
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("workflow_id", workflowId);
        jsonMap.put("applicant_id", applicantId);

        // Ajouter document_id comme un tableau (array)
        Map<String, Object> customData = new HashMap<>();


        jsonMap.put("custom_data", customData);

        // Convertir en JSON string
        String jsonBody = objectMapper.writeValueAsString(jsonMap);

        // Construire la requête HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "workflow_runs"))
                .header("Authorization", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Envoyer la requête et obtenir la réponse
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Afficher la réponse complète pour le débogage
        System.out.println("Réponse API: " + responseBody);

        if (response.statusCode() != 201) { // 201 Created est attendu
            System.err.println("Erreur API: " + response.statusCode() + " - " + responseBody);
            return;
        }

        // Analyser la réponse JSON
        JsonNode jsonResponse = objectMapper.readTree(responseBody);

        // Extraire les informations utiles
        String workflowRunId = jsonResponse.has("id") ? jsonResponse.get("id").asText() : "N/A";
        String status = jsonResponse.has("status") ? jsonResponse.get("status").asText() : "N/A";
        String dashboardUrl = jsonResponse.has("dashboard_url") ? jsonResponse.get("dashboard_url").asText() : "N/A";
        String errorMessage = jsonResponse.has("error") && jsonResponse.get("error").has("message")
                ? jsonResponse.get("error").get("message").asText()
                : "Aucune erreur";

        // Affichage des informations
        System.out.println("✅ Workflow Run ID : " + workflowRunId);
        System.out.println("📌 Statut : " + status);
        System.out.println("🌍 Dashboard URL : " + dashboardUrl);
        System.out.println("⚠️ Erreur : " + errorMessage);
    }

    public static void main(String[] args) {
        try {
            String applicantId = "6678bbc4-9f2f-4c9b-a0d8-da463aa7a17f";
            String workflowId = "7be52188-3b94-494c-b989-0c57b9460a99";
            createWorkflowRun(applicantId, workflowId);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}
