package com.esprit.services;// package com.esprit.services;

import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.SignatureRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloSignService {

    private HelloSignClient client;
    private boolean testMode;

    // Constructeur qui prend la clé API et le paramètre testMode
    public HelloSignService(String apiKey, boolean testMode) {
        client = new HelloSignClient(apiKey);
        this.testMode = testMode;  // Sauvegarder l'état du mode test
    }

    // Méthode pour envoyer un fichier pour signature
    public void sendDocumentForSignature(File document) {
        try {
            // Crée une demande de signature
            SignatureRequest request = new SignatureRequest();
            request.addFile(document); // Ajoute le fichier à la demande
            request.setTitle("Document à signer"); // Définir un titre pour la demande
            request.setSubject("Veuillez signer ce document"); // Sujet du document à signer

            // Ajoute un signataire
            request.addSigner("signer@example.com", "Nom du signataire");

            // Si le mode test est activé, ajoute le paramètre test_mode=1
            if (testMode) {
                request.setTestMode(true);  // Active le mode test dans la demande
            }

            // Envoie la demande de signature à HelloSign
            SignatureRequest response = client.sendSignatureRequest(request); // La réponse est de type SignatureRequest

            // Vérifie si la méthode getId() est disponible
            System.out.println("Demande de signature envoyée avec succès. ID de la signature : " + response.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier l'état de la signature
    public String getSignatureStatus(String requestId) {
        try {
            SignatureRequest request = client.getSignatureRequest(requestId);
            // Utilisez getStatus() au lieu de getStatusCode()
            return "Statut de la demande : " + request.getSignatures().get(0).getStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du statut";
        }
    }


    public void downloadSignedDocument(String requestId, String outputPath) {
        try {
            // Utiliser client.getFiles(requestId) pour obtenir un fichier
            File signedFile = client.getFiles(requestId);

            // Ouvrir un flux d'entrée pour le fichier téléchargé
            try (InputStream fileStream = new FileInputStream(signedFile)) {
                // Créer un fichier de sortie pour enregistrer le fichier signé
                File outputFile = new File(outputPath);

                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    // Lire le contenu du fichier et écrire dans le fichier de sortie
                    while ((bytesRead = fileStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("Document signé téléchargé avec succès : " + outputPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du téléchargement du document signé.");
        } catch (HelloSignException e) {
            throw new RuntimeException(e);
        }
    }


}
