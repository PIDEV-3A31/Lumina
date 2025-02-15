package com.esprit.controllers;

import com.esprit.models.Documents;
import com.esprit.services.ServiceDocuments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class AfficherDocuments {

    @FXML
    private TableView<Documents> table_documents;

    @FXML
    private TableColumn<Documents, Integer> id_document_column;

    @FXML
    private TableColumn<Documents, String> type_document_column;

    @FXML
    private TableColumn<Documents, String> titre_column;

    @FXML
    private TableColumn<Documents, String> description_column;

    @FXML
    private TableColumn<Documents, String> chemin_fichier_column;

    @FXML
    private TableColumn<Documents, String> date_creation_column;

    @FXML
    private TableColumn<Documents, String> date_modification_column;

    @FXML
    private TableColumn<Documents, String> modifier;

    @FXML
    private TableColumn<Documents, String> supprimer;

    private ServiceDocuments serviceDocument = new ServiceDocuments(); // Service pour récupérer les documents

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les attributs de la classe Documents
        id_document_column.setCellValueFactory(new PropertyValueFactory<>("id_document"));
        type_document_column.setCellValueFactory(new PropertyValueFactory<>("type_document"));
        titre_column.setCellValueFactory(new PropertyValueFactory<>("titre"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        chemin_fichier_column.setCellValueFactory(new PropertyValueFactory<>("chemin_fichier"));
        date_creation_column.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        date_modification_column.setCellValueFactory(new PropertyValueFactory<>("date_modification"));

        // Ajout du bouton Supprimer
        supprimer.setCellFactory(createDeleteButtonFactory());

        // Ajout du bouton Modifier
        modifier.setCellFactory(createEditButtonFactory());

        loadData();
    }

    private Callback<TableColumn<Documents, String>, TableCell<Documents, String>> createDeleteButtonFactory() {
        return param -> new TableCell<Documents, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteButton = new Button();
                    ImageView deleteIcon = new ImageView(new Image("assets/supprimer.jpg"));
                    deleteIcon.setFitWidth(20);
                    deleteIcon.setFitHeight(20);
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.getStyleClass().add("transparent-button");
                    deleteButton.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                    deleteButton.setOnAction(event -> {
                        Documents doc = getTableView().getItems().get(getIndex());
                        handleDelete(doc);
                    });
                    setGraphic(deleteButton);
                }
            }
        };
    }

    private Callback<TableColumn<Documents, String>, TableCell<Documents, String>> createEditButtonFactory() {
        return param -> new TableCell<Documents, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button editButton = new Button();
                    ImageView editIcon = new ImageView(new Image("assets/modifier.jpg"));
                    editIcon.setFitWidth(20);
                    editIcon.setFitHeight(20);
                    editButton.setGraphic(editIcon);
                    editButton.getStyleClass().add("transparent-button");
                    editButton.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                    editButton.setOnAction(event -> {
                        Documents doc = getTableView().getItems().get(getIndex());
                        handleEdit(doc);
                    });
                    setGraphic(editButton);
                }
            }
        };
    }

    private void loadData() {
        ObservableList<Documents> documentsList = FXCollections.observableArrayList(serviceDocument.consulter());
        table_documents.setItems(documentsList);
    }

    private void handleDelete(Documents document) {
        System.out.println("Delete document: " + document.getTitre());
        serviceDocument.supprimer(document.getId_document());
        loadData();
    }

    private void handleEdit(Documents document) {
        TextInputDialog dialog = new TextInputDialog(document.getTitre());
        dialog.setTitle("Modifier Document");
        dialog.setHeaderText("Modifier les informations du document");
        dialog.setContentText("Titre :");

        dialog.showAndWait().ifPresent(newTitle -> {
            if (!newTitle.trim().isEmpty()) {
                document.setTitre(newTitle);
                serviceDocument.modifier(document);
                loadData();
            }
        });
    }
}
