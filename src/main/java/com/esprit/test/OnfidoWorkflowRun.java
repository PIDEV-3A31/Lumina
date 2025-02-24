package com.esprit.test;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class OnfidoWorkflowRun {

    private static final String API_URL = "https://api.onfido.com/v3.6/";
    private static final String API_KEY = "api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB";
    private static final OkHttpClient client = new OkHttpClient();

    public static String createWorkflowRun(String applicantId, String workflowId, String documentId) throws IOException {
        // Créer le body JSON avec custom_data comme objet
        String jsonBody = "{\n" +
                "  \"workflow_id\": \"" + workflowId + "\",\n" +
                "  \"applicant_id\": \"" + applicantId + "\",\n" +
                "  \"custom_data\": {\n" +
                "    \"document_id\": \"" + documentId + "\"\n" +
                "  }\n" +
                "}";

        // Créer le corps de la requête en JSON
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));

        // Créer la requête avec l'en-tête d'autorisation approprié
        Request request = new Request.Builder()
                .url(API_URL + "workflow_runs")
                .header("Authorization", "Token token=" + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // Envoyer la requête
        try (Response response = client.newCall(request).execute()) {
            String responseString = response.body() != null ? response.body().string() : null;

            // Afficher la réponse complète pour le débogage
            System.out.println("Réponse API: " + responseString);

            if (!response.isSuccessful()) {
                // Si l'API retourne une erreur, afficher le message détaillé
                System.err.println("Erreur API: " + response.code() + " - " + response.message());
                // Analyser et afficher les détails de l'erreur
                JsonNode jsonResponse = new ObjectMapper().readTree(responseString);
                if (jsonResponse.has("error")) {
                    JsonNode error = jsonResponse.get("error");
                    System.err.println("Détails de l'erreur : " + error.toString());
                } else {
                    System.err.println("Réponse complète d'erreur : " + responseString);
                }
                return null;
            }

            // Analyser la réponse
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            // Retourner l'ID du Workflow Run créé
            return jsonResponse.has("id") ? jsonResponse.get("id").asText() : null;
        }
    }

    public static void main(String[] args) {
        try {
            String applicantId = "3ddf5d5e-50a9-4c2a-8828-721d59eae0ed";  // ID de l'applicant
            String workflowId = "7be52188-3b94-494c-b989-0c57b9460a99";  // ID du workflow
            String documentId = "32a1b919-f4a4-466f-b216-9c9cb76fbae4";  // ID du document
            String workflowRunId = createWorkflowRun(applicantId, workflowId, documentId);

            if (workflowRunId != null) {
                System.out.println("Workflow Run créé avec succès ! ID: " + workflowRunId);
            } else {
                System.out.println("Erreur lors de la création du Workflow Run.");
            }
        } catch (IOException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}
