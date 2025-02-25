package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.models.Reservation;
import come.esprit.services.MailService;
import come.esprit.services.ServiceParking;
import come.esprit.services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import come.esprit.services.ServiceReservation.QRCodeGenerator; // Assurez-vous que cette classe est correctement importée

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class interface_home implements Initializable {

    @FXML
    private TableView<Parking> tableparkings1;

    @FXML
    private TableColumn<Parking, Integer> col_id1;
    @FXML
    private TableColumn<Parking, String> col_name1;
    @FXML
    private TableColumn<Parking, Integer> col_capacity1;
    @FXML
    private TableColumn<Parking, String> col_status1;
    @FXML
    private TableColumn<Parking, String> col_adresses1;
    @FXML
    private TableColumn<Parking, String> col_tarif1;
    @FXML
    private TableColumn<Parking, Integer> col_place1;

    @FXML
    private TextField idpark1;

    @FXML
    private TextField matricule1;
    @FXML
    private TextField search_0;
    @FXML
    private TextField mail;

    @FXML
    private VBox vboxQrCode;

    @FXML
    private ImageView qrImageView;


    @FXML
    private Button add1;

    private ServiceParking serviceParking = new ServiceParking();
    private ServiceReservation serviceReservation = new ServiceReservation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialiser la table des parkings
        col_id1.setCellValueFactory(new PropertyValueFactory<>("id_parck"));
        col_name1.setCellValueFactory(new PropertyValueFactory<>("name_parck"));
        col_capacity1.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        col_status1.setCellValueFactory(new PropertyValueFactory<>("status_parking"));
        col_adresses1.setCellValueFactory(new PropertyValueFactory<>("adresses"));
        col_tarif1.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        col_place1.setCellValueFactory(new PropertyValueFactory<>("places_reservees"));

        loadDataparking();

        // Ajouter un écouteur pour le bouton "Add"
        add1.setOnAction(event -> ajouterReservation());

        // Ajouter un écouteur de sélection pour détecter le parking sélectionné
        tableparkings1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Remplir le champ idpark1 avec l'ID du parking sélectionné
                idpark1.setText(String.valueOf(newValue.getId_parck()));
            }
        });
    }

    private void ajouterReservation() {
        String idParkStr = idpark1.getText();
        String matricule = matricule1.getText();
        String email = mail.getText();  // Récupérer l'email du TextField

        // Vérifier si les champs sont remplis
        if (idParkStr.isEmpty() || matricule.isEmpty() || email.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que l'ID du parking est valide
        int idPark;
        try {
            idPark = Integer.parseInt(idParkStr);
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ID du parking doit être un nombre valide !");
            return;
        }

        // Vérifier que l'email est valide
        if (!email.contains("@") || !email.contains(".")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide !");
            return;
        }

        // Créer la réservation sans ajouter l'email à la classe Reservation
        Reservation reservation = new Reservation(idPark, matricule);

        // Ajouter la réservation
        serviceReservation.ajouter(reservation);

        // Envoyer un e-mail de confirmation
        try {
            MailService.sendEmail(email, "Confirmation de votre réservation", "Bonjour, \n\nNous vous confirmons que votre réservation a été enregistrée avec succès. Nous vous remercions de votre confiance et restons à votre disposition pour toute question.\n\nCordialement,\nL'équipe de réservation'LUMINA'");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'envoi de l'email a échoué.");
            e.printStackTrace();
            return;
        }

        // Afficher un message de succès
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

        // Vider les champs après ajout
        viderChamps();

        // Recharger la liste des parkings ou des réservations si nécessaire
        loadDataparking();
        // Générer les données du QR Code
        String qrData = "Réservation ID: " + reservation.getId_reservation();

        // Créer le répertoire si nécessaire
        File dir = new File("qr_codes");
        if (!dir.exists()) {
            dir.mkdirs();  // Créer le répertoire si nécessaire
        }

        // Chemin pour enregistrer le QR Code
        String qrFilePath = "qr_codes/reservation_" + reservation.getId_reservation() + ".png";

        try {
            QRCodeGenerator.generateQRCode(qrData, qrFilePath);  // Générer le QR Code
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La génération du QR Code a échoué.");
            e.printStackTrace();
            return;
        }

        // Afficher le QR Code dans l'application
        Image qrImage = new Image("file:" + qrFilePath); // Charger l'image du QR Code
        ImageView qrImageView = new ImageView(qrImage);   // Créer un ImageView pour afficher l'image
        qrImageView.setFitWidth(150);  // Ajuster la taille de l'image (si nécessaire)
        qrImageView.setFitHeight(150);
        qrImageView.setPreserveRatio(true);  // Pour conserver les proportions du QR Code

        // Ajouter l'ImageView au layout de l'interface (par exemple un VBox)
        VBox vbox = new VBox(qrImageView);  // Assurez-vous que vous avez un VBox ou un autre conteneur
        vboxQrCode.getChildren().add(vbox); // Ajoute le VBox dans le conteneur principal de la fenêtre

        // Autres actions de la méthode (par exemple, envoyer un email, etc.)
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

        // Rediriger vers une autre vue (Liste des réservations)
       try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home_reservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) add1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void viderChamps() {
        idpark1.clear();
        matricule1.clear();
    }

    private void loadDataparking() {
        ObservableList<Parking> parkings = FXCollections.observableArrayList(serviceParking.afficher());
        tableparkings1.setItems(parkings);
    }
}

