package com.esprit.controllers.Transport;

import com.esprit.models.ligneTransport;
import com.esprit.models.moyenTransport;
import com.esprit.services.serviceMoyenTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReserverTransport {

    private ligneTransport ligneTransport;
    private List<moyenTransport> moyensTransport; // Stocke tous les moyens pour le filtrage

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button reserverButton;



    @FXML
    private ImageView bus, taxi, train;

    @FXML
    public void initialize() {
        // Ajout des gestionnaires d'événements pour filtrer par type
        bus.setOnMouseClicked(event -> filtrerParType("Bus"));
        taxi.setOnMouseClicked(event -> filtrerParType("Taxi"));
        train.setOnMouseClicked(event -> filtrerParType("Train"));

    }


    public void setLigneTransport(ligneTransport ligne) {
        this.ligneTransport = ligne;

        // Charger la liste des moyens de transport associés
        serviceMoyenTransport sm = new serviceMoyenTransport();
        this.moyensTransport = sm.getMoyensByLigneId(ligne.getIdLigne());

        // Afficher les moyens de transport
        afficherMoyens(moyensTransport);
    }


    private void afficherMoyens(List<moyenTransport> moyens) {
        grid.getChildren().clear(); // Nettoyage avant d'ajouter les éléments

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

                // Gestion de la disposition (ex: 2 colonnes par ligne)
                col++;
                if (col == 1) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void filtrerParType(String type) {
        List<moyenTransport> filtres = moyensTransport.stream()
                .filter(m -> m.getTypeTransport().equalsIgnoreCase(type))
                .collect(Collectors.toList());

        afficherMoyens(filtres);
    }
}
