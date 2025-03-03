package com.esprit.controllers.Transport;

import com.esprit.models.profile;
import com.esprit.models.reservation_transport;
import com.esprit.models.user;
import com.esprit.services.serviceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationAdmin {
    private user connectedUser;
    private profile userProfile;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

    @FXML
    private ImageView ajouter_ligne;

    @FXML
    private ImageView ajouter_moyen;

    @FXML
    private TableColumn<reservation_transport, Integer> d_utilisateur_colomn;

    @FXML
    private TableColumn<reservation_transport, LocalDate> date_reservation_Column;

    @FXML
    private TableColumn<reservation_transport, Integer> id_moyenTransport;

    @FXML
    private TableColumn<reservation_transport, Integer> id_reservation_colomn;

    @FXML
    private TableColumn<reservation_transport, Integer> nb_places_Column;

    @FXML
    private TableColumn<reservation_transport, String> statut_column;

    @FXML
    private TableView<reservation_transport> table_lignes_transport;

    @FXML
    private TableColumn<reservation_transport, Double> tarif_total_column;

    @FXML
    private TableColumn<reservation_transport, ?> ligneTransport;

    @FXML
    private TableColumn<reservation_transport, ?> moyenTransport;

    @FXML
    private ImageView return_consulter;

    @FXML
    private ImageView OpenChatBot;



    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés du modèle Reservation
        OpenChatBot.setOnMouseClicked(event -> openChatBotWindow());
        date_reservation_Column.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        nb_places_Column.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        tarif_total_column.setCellValueFactory(new PropertyValueFactory<>("tarifTotal"));
        statut_column.setCellValueFactory(new PropertyValueFactory<>("statut"));



        return_consulter.setOnMouseClicked(event -> retourConsulterTransport());


        loadData();
    }
    @FXML
    private void openChatBotWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatbotUi.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("ChatBot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {

        serviceReservation serviceReservation = new serviceReservation();
        ObservableList<reservation_transport> reservationObservableList = FXCollections.observableArrayList(serviceReservation.consulter());


        table_lignes_transport.setItems(reservationObservableList);

        System.out.println("Chargement des données, nombre de lignes : " + reservationObservableList.size());
    }

    private void retourConsulterTransport() {
        try {
            Stage currentStage = (Stage) return_consulter.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();
            ConsulterLigneTransport controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        System.out.println("jesuisla");
        updateUI();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        if (userProfile.getImage_u() != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                img_current_user.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
        loadImage(userProfile.getImage_u(), img_current_user);
    }
    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + imagePath)).toExternalForm());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }


}
