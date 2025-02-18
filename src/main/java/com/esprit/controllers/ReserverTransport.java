package com.esprit.controllers;

import com.esprit.models.ligneTransport;
import com.esprit.models.moyenTransport;
import com.esprit.models.reservation;
import com.esprit.services.serviceMoyenTransport;
import com.esprit.services.serviceReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class ReserverTransport {
    private ligneTransport ligneTransport;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button reserverButton;

    public void setLigneTransport(ligneTransport ligne) {
        this.ligneTransport = ligne;

        // Charger la liste des moyens de transport associés à cette ligne
        serviceMoyenTransport sm = new serviceMoyenTransport();
        List<moyenTransport> moyens = sm.getMoyensByLigneId(ligne.getIdLigne());

        // Ajouter les moyens de transport à la GridPane
        int row = 0, col = 0;
        for (moyenTransport moyen : moyens) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/itemMoyenTransport.fxml"));
                AnchorPane item = loader.load();

                // Récupérer le contrôleur et lui passer les données
                ItemMoyenTransport controller = loader.getController();
                controller.setData(moyen);

                // Ajouter à la GridPane
                grid.add(item, col, row);

                // Gérer la disposition (ex : 2 colonnes par ligne)
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
