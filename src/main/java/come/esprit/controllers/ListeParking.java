package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.models.Reservation;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListeParking implements Initializable {

    @FXML
    private TableView<Parking> tableparkings;

    @FXML
    private TableColumn<Parking, Integer> col_id;
    @FXML
    private TableColumn<Parking, String> col_name;
    @FXML
    private TableColumn<Parking, Integer> col_capacity;
    @FXML
    private TableColumn<Parking, String> col_status;
    @FXML
    private TableColumn<Parking, String> col_adresses;
    @FXML
    private TableColumn<Parking, String> col_tarif;
    @FXML
    private TableColumn<Parking, Integer> col_place;
    @FXML
    private TableColumn<Parking, String> Supprimer;


    @FXML
    private ImageView ajouter_parking1;


    @FXML
    private TableView<Reservation> table_reservation;
    @FXML
    private TableColumn<Reservation, Integer> col_idres;
    @FXML
    private TableColumn<Reservation, Integer> col_iduser;
    @FXML
    private TableColumn<Reservation, Integer> col_icparking;
    @FXML
    private TableColumn<Reservation, String> col_datereservation;
    @FXML
    private TableColumn<Reservation, String> col_matriculevoiture;
    @FXML
    private TableColumn<Reservation, String> Supprimer_res;

    private ServiceParking serviceParking = new ServiceParking();
    private ServiceReservation serviceReservation = new ServiceReservation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ajouter_parking1.setOnMouseClicked(event -> handleAjouterParking());


        // Initialiser la table des parkings
        col_id.setCellValueFactory(new PropertyValueFactory<>("id_parck"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name_parck"));
        col_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status_parking"));
        col_adresses.setCellValueFactory(new PropertyValueFactory<>("adresses"));
        col_tarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        col_place.setCellValueFactory(new PropertyValueFactory<>("places_reservees"));
        Supprimer.setCellFactory(createDeleteParkingButtonFactory());


        loadDataparking();

        // Initialiser la table des réservations
        col_idres.setCellValueFactory(new PropertyValueFactory<>("id_reservation"));
        col_iduser.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        col_icparking.setCellValueFactory(new PropertyValueFactory<>("id_parck"));
        col_datereservation.setCellValueFactory(new PropertyValueFactory<>("date_reservation"));
        col_matriculevoiture.setCellValueFactory(new PropertyValueFactory<>("matricule_voiture"));
        Supprimer_res.setCellFactory(createDeleteReservationButtonFactory());

        loadDataReservation();
    }

    private void loadDataparking() {
        ObservableList<Parking> parkings = FXCollections.observableArrayList(serviceParking.afficher());
        tableparkings.setItems(parkings);
    }

    private void loadDataReservation() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList(serviceReservation.afficher());
        table_reservation.setItems(reservations);
    }

    private Callback<TableColumn<Parking, String>, TableCell<Parking, String>> createDeleteParkingButtonFactory() {
        return param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteButton = new Button();
                    ImageView deleteIcon = new ImageView(new Image("Assets/supprimer.jpg"));
                    deleteIcon.setFitWidth(20);
                    deleteIcon.setFitHeight(20);
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.getStyleClass().add("transparent-button");

                    deleteButton.setOnAction(event -> {
                        Parking parking = getTableView().getItems().get(getIndex());
                        handleDeleteParking(parking);
                    });

                    setGraphic(deleteButton);
                }
            }
        };
    }







    private Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> createDeleteReservationButtonFactory() {
        return param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteButton = new Button();
                    ImageView deleteIcon = new ImageView(new Image("Assets/supprimer.jpg"));
                    deleteIcon.setFitWidth(20);
                    deleteIcon.setFitHeight(20);
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.getStyleClass().add("transparent-button");

                    deleteButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        handleDeleteReservation(reservation);
                    });

                    setGraphic(deleteButton);
                }
            }
        };
    }

    private void handleDeleteParking(Parking parking) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer ce parking ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonYes) {
                serviceParking.supprimer(parking.getId_parck());

                loadDataparking();
                loadDataReservation();
            }
        });
    }
    private void handleDeleteReservation(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cette réservation ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonYes) {
                serviceReservation.supprimer(reservation.getId_reservation());
                loadDataReservation();
            }
        });

    }

    private void handleAjouterParking() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) ajouter_parking1.getScene().getWindow();

            // Charger la nouvelle interface en arrière-plan
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutparking.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene newScene = new Scene(root);

            // Animation de fondu
            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            // Appliquer la nouvelle scène à la fenêtre actuelle avant de la fermer
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
