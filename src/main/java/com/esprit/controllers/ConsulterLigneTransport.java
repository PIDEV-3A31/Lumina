package com.esprit.controllers;

import com.esprit.models.ligneTransport;
import com.esprit.services.serviceLigneTransport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.InputStream;

public class ConsulterLigneTransport {

    @FXML
    private TableView<ligneTransport> table_lignes_transport;

    @FXML
    private TableColumn<ligneTransport, String> id_ligne;

    @FXML
    private TableColumn<ligneTransport, String> etat_column;

    @FXML
    private TableColumn<ligneTransport, String> horaire_arrivee_column;

    @FXML
    private TableColumn<ligneTransport, String> horaire_depart_column;

    @FXML
    private TableColumn<ligneTransport, String> nom_ligne_column;

    @FXML
    private TableColumn<ligneTransport, Double> tarif_column;

    @FXML
    private TableColumn<ligneTransport, String> zone_couverture_column;

    @FXML
    private TableColumn<ligneTransport, String> supprimerColumn;

    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();

    @FXML
    public void initialize() {
        // Liaison des colonnes aux propriétés de la classe ligneTransport
        id_ligne.setCellValueFactory(new PropertyValueFactory<>("idLigne"));
        nom_ligne_column.setCellValueFactory(new PropertyValueFactory<>("nomLigne"));
        horaire_depart_column.setCellValueFactory(new PropertyValueFactory<>("horaireDepart"));
        horaire_arrivee_column.setCellValueFactory(new PropertyValueFactory<>("horaireArrivee"));
        tarif_column.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        zone_couverture_column.setCellValueFactory(new PropertyValueFactory<>("zoneCouverture"));
        etat_column.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Configuration de la colonne "supprimer" pour afficher un bouton avec l'image de suppression
        supprimerColumn.setCellFactory(new Callback<TableColumn<ligneTransport, String>, TableCell<ligneTransport, String>>() {
            @Override
            public TableCell<ligneTransport, String> call(TableColumn<ligneTransport, String> param) {
                return new TableCell<ligneTransport, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Chargement de l'image depuis le classpath
                            InputStream imageStream = getClass().getResourceAsStream("/assets/delete.png");
                            if (imageStream != null) {
                                Image trashImage = new Image(imageStream);
                                ImageView trashImageView = new ImageView(trashImage);
                                trashImageView.setFitWidth(20);
                                trashImageView.setFitHeight(20);

                                // Création d'un bouton et assignation de l'image
                                Button deleteButton = new Button();
                                deleteButton.setGraphic(trashImageView);
                                deleteButton.getStyleClass().add("transparent-button");

                                // Utilisation de setOnAction pour gérer le clic sur le bouton
                                deleteButton.setOnAction(event -> {
                                    int index = getIndex();
                                    System.out.println("Clic sur le bouton de suppression, index : " + index);
                                    ligneTransport ligne = getTableView().getItems().get(index);
                                    handleDelete(ligne);
                                });
                                setGraphic(deleteButton);
                            } else {
                                System.err.println("Impossible de charger l'image delete.png");
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        // Chargement initial des données dans la TableView
        loadData();
    }

    /**
     * Charge les données des lignes de transport depuis le service et les affiche dans la TableView.
     */
    private void loadData() {
        ObservableList<ligneTransport> lignesTransportList = FXCollections.observableArrayList(serviceLigneTransport.consulter());
        table_lignes_transport.setItems(lignesTransportList);
        System.out.println("Chargement des données, nombre de lignes : " + lignesTransportList.size());
    }

    /**
     * Gère la suppression d'une ligne de transport.
     *
     * @param ligne la ligne de transport à supprimer
     */
    private void handleDelete(ligneTransport ligne) {
        System.out.println("Suppression de la ligne : " + ligne.getNomLigne() + " (ID: " + ligne.getIdLigne() + ")");
        serviceLigneTransport.supprimer(ligne.getIdLigne());
        loadData();
    }
}
