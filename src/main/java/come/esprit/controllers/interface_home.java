package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.models.Reservation;
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
import javafx.stage.Stage;

import javax.swing.*;
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
        // Récupérer l'ID du parking sous forme de String puis le convertir en Integer
        String idParkStr = idpark1.getText();
        String matricule = matricule1.getText();

        // Vérifier si les champs sont remplis
        if (idParkStr.isEmpty() || matricule.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que l'ID du parking est valide (convertir en Integer)
        int idPark;
        try {
            idPark = Integer.parseInt(idParkStr);  // Convertir la chaîne en entier
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ID du parking doit être un nombre valide !");
            return;
        }

        // Créer la réservation (id_reservation est auto-incrémenté, donc non inclus)
        Reservation reservation = new Reservation(idPark, matricule);

        // Ajouter la réservation
        serviceReservation.ajouter(reservation);

        // Afficher un message de succès
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

        // Vider les champs après ajout
        viderChamps();

        // Recharger la liste des parkings ou des réservations si nécessaire
        loadDataparking();

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

