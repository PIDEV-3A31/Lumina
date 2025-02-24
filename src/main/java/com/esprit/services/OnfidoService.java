package com.esprit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import com.google.gson.JsonObject;
import java.io.IOException;

public class OnfidoService {
    private static final String API_URL = "https://api.onfido.com/v3.6";
    private static final String API_KEY = "api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB";

    private final OkHttpClient client = new OkHttpClient();

    public String createApplicant(String firstName, String lastName) throws IOException {
        // Créer un JSON pour l'envoi
        String jsonData = "{ \"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\" }";

        RequestBody body = RequestBody.create(jsonData, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL + "/applicants")
                .header("Authorization", API_KEY)
                .header("Authorization", "Token token=" + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erreur API: " + response.code() + " - " + response.message());
                return null;
            }

            String responseString = response.body() != null ? response.body().string() : null;
            System.out.println("Réponse de l'API: " + responseString);

            // Parser la réponse JSON et récupérer l'ID de l'applicant
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            if (jsonResponse.has("id")) {
                return jsonResponse.get("id").asText(); // Retourne l'ID du demandeur
            } else {
                System.err.println("L'ID de l'applicant n'a pas été trouvé !");
                return null;
            }
        }
    }




}
