package com.esprit.controllers;

import com.esprit.models.reservation;
import com.esprit.services.serviceReservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
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
    private TableColumn<reservation, ?> ligneTransport;

    @FXML
    private TableColumn<reservation, ?> moyenTransport;

    @FXML
    private ImageView return_consulter;



    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés du modèle Reservation

        date_reservation_Column.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        nb_places_Column.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        tarif_total_column.setCellValueFactory(new PropertyValueFactory<>("tarifTotal"));
        statut_column.setCellValueFactory(new PropertyValueFactory<>("statut"));



        return_consulter.setOnMouseClicked(event -> retourConsulterTransport());


        loadData();
    }

    private void loadData() {

        serviceReservation serviceReservation = new serviceReservation();
        ObservableList<reservation> reservationObservableList = FXCollections.observableArrayList(serviceReservation.consulter());


        table_lignes_transport.setItems(reservationObservableList);

        System.out.println("Chargement des données, nombre de lignes : " + reservationObservableList.size());
    }

    private void retourConsulterTransport() {
        try {
            Stage currentStage = (Stage) return_consulter.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
