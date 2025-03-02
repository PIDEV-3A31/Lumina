package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.models.Reservation;
import come.esprit.services.MailService;
import come.esprit.services.ServiceParking;
import come.esprit.services.ServiceReservation;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

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
    @FXML
    private Button add11;


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

        add11.setOnMouseClicked(event -> retourReservation());


        // Ajouter un écouteur de sélection pour détecter le parking sélectionné
        tableparkings1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Remplir le champ idpark1 avec l'ID du parking sélectionné
                idpark1.setText(String.valueOf(newValue.getId_parck()));
            }
        });




    }






    private void ajouterReservation() {
        String idParkStr = idpark1.getText().trim();
        String matricule = matricule1.getText().trim();
        String email = mail.getText().trim();

        // Vérification rapide des champs vides
        if (idParkStr.isEmpty() || matricule.isEmpty() || email.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification de l'ID du parking
        int idPark;
        try {
            idPark = Integer.parseInt(idParkStr);
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ID du parking doit être un nombre valide !");
            return;
        }

        // Vérification de l'email
        if (!email.contains("@") || !email.contains(".")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide !");
            return;
        }

        // Création et ajout de la réservation
        Reservation reservation = new Reservation(idPark, matricule);
        serviceReservation.ajouter(reservation);

        // Envoi de l'e-mail
        try {
            MailService.sendEmail(email, "Confirmation de votre réservation",
                    "Bonjour,\n\nNous vous confirmons votre réservation.\n\nCordialement,\nL'équipe 'LUMINA'");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'envoi de l'email a échoué.");
            return;
        }

        // Génération du QR Code
        String qrData = "Réservation ID: " + reservation.getId_reservation();
        String qrFilePath = "qr_codes/reservation_" + reservation.getId_reservation() + ".png";

        // Vérifier et créer le répertoire une seule fois
        new File("qr_codes").mkdirs();

        try {
            QRCodeGenerator.generateQRCode(qrData, qrFilePath);
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La génération du QR Code a échoué.");
            return;
        }

        // Affichage du QR Code
        vboxQrCode.getChildren().clear();  // Nettoyage avant ajout
        ImageView qrImageView = new ImageView(new Image("file:" + qrFilePath));
        qrImageView.setFitWidth(150);
        qrImageView.setFitHeight(150);
        qrImageView.setPreserveRatio(true);
        vboxQrCode.getChildren().add(qrImageView);

        // Message de succès
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

        // Redirection vers la liste des réservations
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/home_reservation.fxml"));
            Stage stage = (Stage) add1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des réservations.");
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



    private void retourReservation() {
        try {
            Stage currentStage = (Stage) add11.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home_reservation.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);

            // Animation de fondu
            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

