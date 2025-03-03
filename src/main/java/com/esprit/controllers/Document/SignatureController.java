package com.esprit.controllers.Document;

import com.esprit.services.HelloSignService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;


public class SignatureController implements Initializable {

    @FXML
    private Label fileLabel;

    @FXML
    private Label statusLabel;  // Added status label to display signature status

    private File document;

    @FXML
    private WebView webView;

    private WebEngine webEngine;


    // Méthode pour choisir un fichier

    public void initialize(URL location, ResourceBundle resources) {

        loadHelloSignPage();

        webEngine = webView.getEngine();

        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            System.out.println("URL chargée : " + newLocation);

            if (newLocation.endsWith(".pdf")) { // Vérifie si l'URL est un fichier PDF
                downloadFile(newLocation);
                webEngine.getLoadWorker().cancel(); // Annuler le chargement pour éviter l'affichage
            }
        });

    }

    // Méthode pour charger la page de gestion HelloSign
    private void loadHelloSignPage() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://app.hellosign.com/home/manage?status=all");
    }

    private void downloadFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // Timeout en cas de connexion trop longue
            connection.setReadTimeout(5000); // Timeout en cas de lecture trop longue

            // Vérifiez les en-têtes de la réponse
            int responseCode = connection.getResponseCode();
            System.out.println("Code de réponse : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {  // Vérifie si le fichier est accessible
                // Ouvrir FileChooser pour enregistrer le fichier
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                fileChooser.setInitialFileName("document.pdf");
                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    try (InputStream in = connection.getInputStream();
                         FileOutputStream out = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                        statusLabel.setText("Fichier téléchargé : " + file.getAbsolutePath());
                    }
                }
            } else {
                System.out.println("Erreur lors du téléchargement : Code de réponse " + responseCode);
                statusLabel.setText("Échec du téléchargement, code de réponse : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Échec du téléchargement !");
        }
    }


    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        document = fileChooser.showOpenDialog(new Stage());

        if (document != null) {
            fileLabel.setText("Fichier sélectionné : " + document.getName());
        } else {
            fileLabel.setText("Aucun fichier sélectionné.");
        }
    }

    // Méthode pour signer le document
    @FXML
    public void signDocument() {
        if (document != null) {
            // Appel à HelloSign pour envoyer le document à signer
            HelloSignService service = new HelloSignService("628df1382f45dacaf2ef81e74551e23127362d54b1a0de5f0037bb1aea63c322", true);
            service.sendDocumentForSignature(document);
            statusLabel.setText("Document envoyé pour signature.");  // Provide feedback to the user
        } else {
            fileLabel.setText("Veuillez d'abord choisir un fichier.");
        }
    }


}