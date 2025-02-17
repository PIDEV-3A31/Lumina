package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import com.esprit.models.ligneTransport;
import com.esprit.services.serviceLigneTransport;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InterfaceUserTransport implements Initializable {

    @FXML
    private Label horaire_arrivee_label;

    @FXML
    private Label horaire_depart_label;

    @FXML
    private Label lieux_arriveLabel;

    @FXML
    private Label lieux_departLabel;

    @FXML
    private ImageView mapImage;

    @FXML
    private ImageView return_home;

    @FXML
    private Label tarif_label;



    @FXML
    private Label zone_couverture_label;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    private serviceLigneTransport ligneTransportService = new serviceLigneTransport();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<ligneTransport> lignes = ligneTransportService.consulter();
        int column = 0;
        int row = 1;

        try {
            for (ligneTransport ligne : lignes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/itemLigneTransport.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemLigneTransport itemController = fxmlLoader.getController();
                itemController.setData(ligne, this);

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche les détails de la ligne sélectionnée
     */
    public void afficherDetailsLigneTransport(ligneTransport ligneTransport) {
        lieux_departLabel.setText(ligneTransport.getLieuxDepart());
        lieux_arriveLabel.setText(ligneTransport.getLieuxArrive());
        horaire_depart_label.setText(ligneTransport.getHoraireDepart().toString());
        horaire_arrivee_label.setText(ligneTransport.getHoraireArrivee().toString());
        tarif_label.setText(String.valueOf(ligneTransport.getTarif()));
        zone_couverture_label.setText(ligneTransport.getZoneCouverture());
    }
}
