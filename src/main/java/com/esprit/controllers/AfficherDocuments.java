package com.esprit.controllers;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import com.esprit.services.ServiceDemande;
import com.esprit.services.ServiceDocuments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class AfficherDocuments {

    @FXML
    private TableView<Documents> table_documents;


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

    @FXML
    private ImageView ajouter_documents;

    @FXML
    private TableView<Demandes> table_demande;



    @FXML
    private TableColumn<Demandes, String> date_demande;

    @FXML
    private TableColumn<Demandes, String> status_demande;

    @FXML
    private TableColumn<Demandes, String> description;

    @FXML
    private TableColumn<Demandes, String> type_document;

    @FXML
    private TableColumn<Demandes, String> supprimerDemande;

    @FXML
    private Button gererButton;

    @FXML
    private Button valider_button;

    private Documents documentSelectionne;
    private Demandes demandeSelectionnee;





    private ServiceDocuments serviceDocument = new ServiceDocuments();
    private ServiceDemande serviceDemande = new ServiceDemande();
    private Demandes selectedDemande;  // Variable pour stocker la demande sélectionnée



    @FXML
    public void initialize() {

        ajouter_documents.setOnMouseClicked(event -> handleAjouterDocument());
        table_documents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            documentSelectionne = newSelection; // Stocke le document sélectionné
        });

        table_demande.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            demandeSelectionnee = newSelection; // Stocke la demande sélectionnée
        });


        type_document_column.setCellValueFactory(new PropertyValueFactory<>("type_document"));
        titre_column.setCellValueFactory(new PropertyValueFactory<>("titre"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        chemin_fichier_column.setCellValueFactory(new PropertyValueFactory<>("chemin_fichier"));
        date_creation_column.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        date_modification_column.setCellValueFactory(new PropertyValueFactory<>("date_modification"));

        supprimer.setCellFactory(createDeleteButtonFactory());

        modifier.setCellFactory(createEditButtonFactory());



        loadDataDocument();


        date_demande.setCellValueFactory(new PropertyValueFactory<>("date_demande"));
        status_demande.setCellValueFactory(new PropertyValueFactory<>("statut_demande"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type_document.setCellValueFactory(new PropertyValueFactory<>("type_document"));

        supprimerDemande.setCellFactory(createDeleteDemandeButtonFactory());

        loadDataDemandes();

        gererButton.setVisible(false);

        table_demande.setOnMouseClicked(event -> {
            selectedDemande = table_demande.getSelectionModel().getSelectedItem();
            if (selectedDemande != null) {
                gererButton.setVisible(true);  // Afficher le bouton "Gérer"
            }
        });

        gererButton.setOnAction(event -> {
            if (selectedDemande != null && documentSelectionne != null) {
                handleGerer();
            } else {
                System.out.println("Veuillez sélectionner une demande et un document.");
            }
        });


        valider_button.setOnAction(event -> {
            if (selectedDemande != null) {
                handleValiderDemande(selectedDemande);
                loadDataDemandes();
            }
        });
        loadDataDemandes();
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

    private Callback<TableColumn<Demandes, String>, TableCell<Demandes, String>> createDeleteDemandeButtonFactory() {
        return param -> new TableCell<Demandes, String>() {
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
                        Demandes demande = getTableView().getItems().get(getIndex());
                        handleDeleteDemande(demande);
                    });
                    setGraphic(deleteButton);
                }
            }
        };
    }



    private void loadDataDocument() {
        ObservableList<Documents> documentsList = FXCollections.observableArrayList(serviceDocument.consulter());
        table_documents.setItems(documentsList);
    }

    private void loadDataDemandes() {
        ObservableList<Demandes> demandesList = FXCollections.observableArrayList(serviceDemande.consulter());
        table_demande.setItems(demandesList);
    }

    private void handleDelete(Documents document) {
        serviceDocument.supprimer(document.getId_document());
        loadDataDocument();
    }

    private void handleDeleteDemande(Demandes demande) {
        serviceDemande.supprimer(demande.getId_demande());
        loadDataDemandes();
    }

    @FXML
    private void handleAjouterDocument() {
        try {
            Stage currentStage = (Stage) ajouter_documents.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumAjouterDocument.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root);

            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(Documents document) {
        try {
            Stage currentStage = (Stage) table_documents.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumModifierDocument.fxml"));
            Parent root = loader.load();

            ModifierDocumentController controller = loader.getController();
            controller.setDocument(document, serviceDocument);

            Scene newScene = new Scene(root);

            currentStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleGerer() {
        if (demandeSelectionnee != null && documentSelectionne != null) {
            String status = demandeSelectionnee.getStatut_demande();

            if ("nouvelle".equals(status)) {
                // Affecter l'id du document à la demande
                demandeSelectionnee.setId_document(documentSelectionne.getId_document());
                demandeSelectionnee.setStatut_demande("en cours");

                // Mettre à jour la demande dans la base de données
                ServiceDemande serviceDemande = new ServiceDemande();
                serviceDemande.modifier(demandeSelectionnee);

                System.out.println("Document ID " + documentSelectionne.getId_document() + " affecté à la demande.");
                loadDataDemandes();
            } else if ("en cours".equals(status)) {
                handleModifyDocumentForDemande(demandeSelectionnee);
            }
        } else {
            System.out.println("Veuillez sélectionner un document et une demande.");
        }
    }


    private void handleAjouterDocumentForDemande(Demandes demande) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterIdDocumentDemande.fxml"));
            Parent root = loader.load();

            ajouterIdDocument controller = loader.getController();
            controller.setDemande(demande, serviceDemande);

            Stage stage = new Stage();
            stage.setTitle("Ajouter ID Document à la Demande");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            loadDataDemandes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void handleModifyDocumentForDemande(Demandes demande) {
        Documents document = serviceDocument.getDocumentForDemande(demande.getId_demande());
        if (document != null) {
            try {
                Stage currentStage = (Stage) ajouter_documents.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumModifierDocument.fxml"));
                Parent root = loader.load();

                ModifierDocumentController controller = loader.getController();
                controller.setDocument(document, serviceDocument);

                Scene newScene = new Scene(root);
                currentStage.setScene(newScene);

                serviceDemande.modifier(demande);
                loadDataDemandes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleValiderDemande(Demandes demande) {
        if (demande != null && "en cours".equals(demande.getStatut_demande())) {
            demande.setStatut_demande("validée");

            serviceDemande.modifier(demande);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText(null);
            alert.setContentText("Le statut de la demande a été mis à jour en 'validée'.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Action non autorisée");
            alert.setHeaderText(null);
            alert.setContentText("Le statut de la demande ne peut pas être modifié.");
            alert.showAndWait();
        }
    }



}
