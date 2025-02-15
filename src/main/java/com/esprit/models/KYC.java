package com.esprit.models;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.io.UnsupportedEncodingException;

public class KYC {
    private static final String API_KEY = "api_sandbox.XYgAch5UAbc.zmjrSDL4rUcWmpHN8cR4tQT0vs2f7dCB";
    private static final String API_URL = "https://api.onfido.com/v3.4/";
    private static final String API_URL_DOCUMENT = "https://api.onfido.com/v3.4/documents";  // URL de l'API pour télécharger un document
    private static final String API_URL_STATUS = "https://api.onfido.com/v3.4/checks/{check_id}";  // URL de l'API pour consulter l'état de la vérification
    private static final String API_URL_CHECKS = "https://api.onfido.com/v3.4/checks";

    // Méthode pour créer un applicant
    public static String createApplicant() throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(API_URL + "applicants");

        // Configuration des en-têtes HTTP
        postRequest.setHeader("Authorization", "Token token=" + API_KEY);
        postRequest.setHeader("Content-Type", "application/json");

        // Créer les données JSON à envoyer dans la requête
        String jsonData = "{ \"first_name\": \"Jhon\", \"last_name\": \"Doe\", \"dob\": \"1990-01-01\" }";
        postRequest.setEntity(new StringEntity(jsonData));

        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            System.out.println("Réponse de l'API: " + responseString);

            // Utiliser Jackson pour parser la réponse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);
            String applicantId = jsonResponse.get("id").asText();
            return applicantId;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Return null if something goes wrong
    }
    public static void uploadDocumentAndCreateCheck(String applicantId, String filePath) {
        try {
            // Step 1: Upload a document for verification
            String documentId = uploadDocument(applicantId, filePath);
            if (documentId != null) {
                System.out.println("Document uploaded successfully with ID: " + documentId);

                // Step 2: Create a check for the applicant with the document ID
                String checkId = createCheck(applicantId, documentId);  // Pass the documentId to create check
                if (checkId != null) {
                    System.out.println("Check ID: " + checkId);

                    // Step 3: Check the status of the verification
                    checkVerificationStatus(checkId);
                } else {
                    System.out.println("Check creation failed.");
                }
            } else {
                System.out.println("Document upload failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String uploadDocument(String applicantId, String filePath) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(API_URL_DOCUMENT);
            postRequest.setHeader("Authorization", "Token token=" + API_KEY);
            postRequest.setHeader("Content-Type", "multipart/form-data");

            // Add the file to the request
            File file = new File(filePath);
            FileBody fileBody = new FileBody(file);
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addPart("file", fileBody)
                    .addTextBody("applicant_id", applicantId)
                    .build();
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println("Réponse de l'API (document upload): " + responseString);

                // Extract document ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseString);
                String documentId = jsonResponse.get("id").asText();
                return documentId;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createCheck(String applicantId, String documentId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(API_URL_CHECKS);
            postRequest.setHeader("Authorization", "Token token=" + API_KEY);
            postRequest.setHeader("Content-Type", "application/json");

            // Create the JSON data with document ID for check creation
            String jsonData = "{ \"applicant_id\": \"" + applicantId + "\", \"report_names\": [\"document\"], \"document_id\": \"" + documentId + "\" }";
            postRequest.setEntity(new StringEntity(jsonData));

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                System.out.println("Réponse de l'API (check creation): " + responseString);

                // Extract check ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseString);
                String checkId = jsonResponse.get("id").asText();
                return checkId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String extractCheckId (String responseString) {
        String checkId = null;
        try {
            // Example response parsing: you can use a library like Gson or Jackson to parse JSON
            // Assuming the response is in JSON format and check_id is returned as part of the response body
            int checkIdIndex = responseString.indexOf("check_id");
            if (checkIdIndex != -1) {
                checkId = responseString.substring(checkIdIndex + 11, responseString.indexOf(",", checkIdIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkId;
    }

    // Vérifier l'état de la vérification KYC
    public static void checkVerificationStatus(String checkId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Remplacer {check_id} dans l'URL
            String url = API_URL_STATUS.replace("{check_id}", checkId);
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Authorization", "Token token=" + API_KEY);

            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                System.out.println("Réponse de l'API: " + responseString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





    }

}
