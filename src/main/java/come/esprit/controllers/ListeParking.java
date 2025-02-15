package come.esprit.controllers;

import come.esprit.models.Parking;
import come.esprit.services.ServiceParking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
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

    private ServiceParking service = new ServiceParking(); // Service pour récupérer les parkings

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Lier les colonnes aux attributs de Parking
        col_id.setCellValueFactory(new PropertyValueFactory<>("id_parck"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name_parck"));
        col_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status_parking"));
        col_adresses.setCellValueFactory(new PropertyValueFactory<>("adresses"));
        col_tarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        col_place.setCellValueFactory(new PropertyValueFactory<>("places_reservees"));

        // Charger les données dans la table
        loadData();
    }

    private void loadData() {
        ObservableList<Parking> park = FXCollections.observableArrayList(service.afficher());

        tableparkings.setItems(park);
    }
}
