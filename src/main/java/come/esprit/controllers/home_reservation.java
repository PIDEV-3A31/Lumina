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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home_reservation implements Initializable {


    @FXML
    private TableView<Reservation> table_reservation1;
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




        // Initialiser la table des réservations
        col_idres.setCellValueFactory(new PropertyValueFactory<>("id_reservation"));
        col_iduser.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        col_icparking.setCellValueFactory(new PropertyValueFactory<>("id_parck"));
        col_datereservation.setCellValueFactory(new PropertyValueFactory<>("date_reservation"));
        col_matriculevoiture.setCellValueFactory(new PropertyValueFactory<>("matricule_voiture"));
        //Supprimer_res.setCellFactory(createDeleteReservationButtonFactory());

        loadDataReservation();
    }


    private void loadDataReservation() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList(serviceReservation.afficher());
        table_reservation1.setItems(reservations);
    }

}

