package com.esprit.controllers;

import com.esprit.models.ligneTransport;
import com.esprit.models.moyenTransport;
import com.esprit.services.serviceLigneTransport;
import com.esprit.services.serviceMoyenTransport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;



public class ConsulterLigneTransport {

    @FXML
    private Button GoToMunicipality;

    @FXML
    private Button GoToParking;

    @FXML
    private Button GoToTraffic;

    @FXML
    private Button GoToUser;

    @FXML
    private ImageView ajouter_ligne;

    @FXML
    private ImageView ajouter_moyen;

    @FXML
    private TableView<ligneTransport> table_lignes_transport;

    @FXML
    private TableView<moyenTransport> table_lignes_moyen;

    // Colonnes pour la table des lignes de transport
    @FXML
    private TableColumn<ligneTransport, String> nom_ligne_column;

    @FXML
    private TableColumn<ligneTransport, String> zone_couverture_column;

    @FXML
    private TableColumn<ligneTransport, Double> tarif_column;

    @FXML
    private TableColumn<ligneTransport, String> lieux_depart_column;

    @FXML
    private TableColumn<ligneTransport, String> lieux_arrivee_column;

    @FXML
    private TableColumn<ligneTransport, String> horaire_arrivee_column;

    @FXML
    private TableColumn<ligneTransport, String> horaire_depart_column;

    @FXML
    private TableColumn<ligneTransport, String> etat_column;

    @FXML
    private TableColumn<ligneTransport, String> supprimer_ligneColumn;

    // Colonnes pour la table des moyens de transport
    @FXML
    private TableColumn<moyenTransport, String> immatriculation_column;

    @FXML
    private TableColumn<moyenTransport, String> type_transport_colomn;

    @FXML
    private TableColumn<moyenTransport, Integer> capacite_max_colomn;

    @FXML
    private TableColumn<moyenTransport, Integer> nb_places_Column;

    @FXML
    private TableColumn<moyenTransport, String> etat_Column;

    @FXML
    private TableColumn<moyenTransport, String> supprimer_moyenColumn;

    @FXML
    private TableColumn<ligneTransport, Void> modifier_ligneColumn;

    @FXML
    private TableColumn<moyenTransport, Void> modifier_moyenColumn;


    @FXML
    private Label conculter_reservation;


    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();
    private final serviceMoyenTransport serviceMoyenTransport = new serviceMoyenTransport();

    @FXML
    public void initialize() {

        GoToTraffic.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/gestion_traffic.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        GoToMunicipality.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/fxml/afficherDocuments.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // Configuration des colonnes pour les lignes de transport
        nom_ligne_column.setCellValueFactory(new PropertyValueFactory<>("nomLigne"));
        zone_couverture_column.setCellValueFactory(new PropertyValueFactory<>("zoneCouverture"));
        tarif_column.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        lieux_depart_column.setCellValueFactory(new PropertyValueFactory<>("lieuxDepart"));
        lieux_arrivee_column.setCellValueFactory(new PropertyValueFactory<>("lieuxArrive"));
        horaire_depart_column.setCellValueFactory(new PropertyValueFactory<>("horaireDepart"));
        horaire_arrivee_column.setCellValueFactory(new PropertyValueFactory<>("horaireArrivee"));
        etat_column.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Configuration des colonnes pour les moyens de transport
        immatriculation_column.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        type_transport_colomn.setCellValueFactory(new PropertyValueFactory<>("typeTransport"));
        capacite_max_colomn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        nb_places_Column.setCellValueFactory(new PropertyValueFactory<>("place_reservees"));
        etat_Column.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Configuration des boutons de suppression
        setupDeleteButton(supprimer_ligneColumn, table_lignes_transport, serviceLigneTransport);
        setupDeleteButton(supprimer_moyenColumn, table_lignes_moyen, serviceMoyenTransport);

        ajouter_ligne.setOnMouseClicked(event -> handleAddLigne());
        ajouter_moyen.setOnMouseClicked(event -> handleAddMoyen());
        conculter_reservation.setOnMouseClicked(event -> handleReservation());





        modifier_ligneColumn.setCellFactory(param -> new TableCell<ligneTransport, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    InputStream imageStream = getClass().getResourceAsStream("/assets/update.png");
                    if (imageStream != null) {
                        Image updateImage = new Image(imageStream);
                        ImageView updateImageView = new ImageView(updateImage);
                        updateImageView.setFitWidth(20);
                        updateImageView.setFitHeight(20);

                        Button editButton = new Button();
                        editButton.setGraphic(updateImageView);
                        editButton.getStyleClass().add("transparent-button");

                        editButton.setOnAction(event -> {
                            ligneTransport ligne = getTableView().getItems().get(getIndex());
                            handleEditLigne(ligne);
                        });

                        setGraphic(editButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });


        modifier_moyenColumn.setCellFactory(param -> new TableCell<moyenTransport, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    InputStream imageStream = getClass().getResourceAsStream("/assets/update.png");
                    if (imageStream != null) {
                        Image updateImage = new Image(imageStream);
                        ImageView updateImageView = new ImageView(updateImage);
                        updateImageView.setFitWidth(20);
                        updateImageView.setFitHeight(20);

                        Button editButton = new Button();
                        editButton.setGraphic(updateImageView);
                        editButton.getStyleClass().add("transparent-button");

                        editButton.setOnAction(event -> {
                            moyenTransport moyen = getTableView().getItems().get(getIndex());
                            handleEditMoyen(moyen);
                        });

                        setGraphic(editButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });


        loadData();
    }



    private <T> void setupDeleteButton(TableColumn<T, String> column, TableView<T> table, Object service) {
        column.setCellFactory(param -> new TableCell<T, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    InputStream imageStream = getClass().getResourceAsStream("/assets/delete.png");
                    if (imageStream != null) {
                        Image trashImage = new Image(imageStream);
                        ImageView trashImageView = new ImageView(trashImage);
                        trashImageView.setFitWidth(20);
                        trashImageView.setFitHeight(20);

                        Button deleteButton = new Button();
                        deleteButton.setGraphic(trashImageView);
                        deleteButton.getStyleClass().add("transparent-button");
                        deleteButton.setOnAction(event -> {
                            int index = getIndex();
                            T itemToDelete = table.getItems().get(index);
                            handleDelete(itemToDelete, service, table);
                        });
                        setGraphic(deleteButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private <T> void handleDelete(T item, Object service, TableView<T> table) {
        if (item instanceof ligneTransport) {
            serviceLigneTransport.supprimer(((ligneTransport) item).getIdLigne());
        } else if (item instanceof moyenTransport) {
            serviceMoyenTransport.supprimer(((moyenTransport) item).getIdMoyenTransport());
        }
        loadData();
    }

    private void loadData() {
        ObservableList<ligneTransport> lignesTransportList = FXCollections.observableArrayList(serviceLigneTransport.consulter());
        table_lignes_transport.setItems(lignesTransportList);

        ObservableList<moyenTransport> moyensTransportList = FXCollections.observableArrayList(serviceMoyenTransport.consulter());
        table_lignes_moyen.setItems(moyensTransportList);
    }
    private void handleEditLigne(ligneTransport ligne) {
        try {
            Stage currentStage = (Stage) table_lignes_transport.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier_ligneTransport.fxml"));
            Parent root = loader.load();

            ModifierLigneTransport controller = loader.getController();
            controller.setLigneTransport(ligne);

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void handleRedirection(javafx.scene.input.MouseEvent  event, String fxmlPath) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        // Récupérer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Changer la scène
        stage.setScene(scene);
        stage.show();
    }

    private void handleEditMoyen(moyenTransport moyen) {
        try {
            Stage currentStage = (Stage) table_lignes_moyen.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier_moyenTransport.fxml"));
            Parent root = loader.load();

            ModifierMoyenTransport controller = loader.getController();
            controller.setMoyenTransport(moyen);

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleAddLigne() {
        try {
            Stage currentStage = (Stage) ajouter_ligne.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter_ligneTransport.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir l'interface Ajouter Moyen Transport
    private void handleAddMoyen() {
        try {
            Stage currentStage = (Stage) ajouter_moyen.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter_moyenTransport.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Méthode pour ouvrir l'interface Ajouter Moyen Transport
    private void handleReservation() {
        try {
            Stage currentStage = (Stage) ajouter_moyen.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservationAdmin.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}