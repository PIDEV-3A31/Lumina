package com.esprit.controllers;

import com.esprit.models.reservation;
import com.esprit.services.serviceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.util.List;

public class ReservationAdmin {

    @FXML
    private ImageView ajouter_ligne;

    @FXML
    private ImageView ajouter_moyen;

    @FXML
    private TableColumn<reservation, Integer> d_utilisateur_colomn;

    @FXML
    private TableColumn<reservation, LocalDate> date_reservation_Column;

    @FXML
    private TableColumn<reservation, Integer> id_moyenTransport;

    @FXML
    private TableColumn<reservation, Integer> id_reservation_colomn;

    @FXML
    private TableColumn<reservation, Integer> nb_places_Column;

    @FXML
    private TableColumn<reservation, String> statut_column;

    @FXML
    private TableView<reservation> table_lignes_transport;

    @FXML
    private TableColumn<reservation, Double> tarif_total_column;



    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés du modèle Reservation
        id_reservation_colomn.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        d_utilisateur_colomn.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        id_moyenTransport.setCellValueFactory(new PropertyValueFactory<>("idMoyenTransport"));
        date_reservation_Column.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        nb_places_Column.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        tarif_total_column.setCellValueFactory(new PropertyValueFactory<>("tarifTotal"));
        statut_column.setCellValueFactory(new PropertyValueFactory<>("statut"));


        loadData();
    }

    private void loadData() {

        serviceReservation serviceReservation = new serviceReservation();
        ObservableList<reservation> reservationObservableList = FXCollections.observableArrayList(serviceReservation.consulter());


        table_lignes_transport.setItems(reservationObservableList);

        System.out.println("Chargement des données, nombre de lignes : " + reservationObservableList.size());
    }


}
