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

    @FXML
    private ImageView ajouter_documents;

    @FXML
    private TableView<Demandes> table_demande;

    @FXML
    private TableColumn<Demandes, Integer> id_demande;

    @FXML
    private TableColumn<Demandes, Integer> id_utilisateur;

    @FXML
    private TableColumn<Demandes, Integer> id_document;

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





    private ServiceDocuments serviceDocument = new ServiceDocuments();
    private ServiceDemande serviceDemande = new ServiceDemande();
    private Demandes selectedDemande;  // Variable pour stocker la demande sélectionnée

    private Demandes demande;


    @FXML
    public void initialize() {

        ajouter_documents.setOnMouseClicked(event -> handleAjouterDocument());

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



        loadDataDocument();


        // Liaison des colonnes avec les attributs de la classe Demandes
        id_demande.setCellValueFactory(new PropertyValueFactory<>("id_demande"));
        id_utilisateur.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
        id_document.setCellValueFactory(new PropertyValueFactory<>("id_document"));
        date_demande.setCellValueFactory(new PropertyValueFactory<>("date_demande"));
        status_demande.setCellValueFactory(new PropertyValueFactory<>("statut_demande"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type_document.setCellValueFactory(new PropertyValueFactory<>("type_document"));

        supprimerDemande.setCellFactory(createDeleteDemandeButtonFactory());

        loadDataDemandes();

        // Cacher initialement le bouton "Gérer"
        gererButton.setVisible(false);

        // Détecter le clic sur une ligne de la table des demandes
        table_demande.setOnMouseClicked(event -> {
            selectedDemande = table_demande.getSelectionModel().getSelectedItem();
            if (selectedDemande != null) {
                gererButton.setVisible(true);  // Afficher le bouton "Gérer"
            }
        });

        // Ajouter l'action pour le bouton "Gérer"
        gererButton.setOnAction(event -> {
            if (selectedDemande != null) {
                handleGerer(selectedDemande);
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
        //System.out.println("Delete document: " + document.getTitre());
        serviceDocument.supprimer(document.getId_document());
        loadDataDocument();
    }

    private void handleDeleteDemande(Demandes demande) {
        //System.out.println("Suppression de la demande : " + demande.getId_demande());
        serviceDemande.supprimer(demande.getId_demande());
        loadDataDemandes();
    }

    @FXML
    private void handleAjouterDocument() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) ajouter_documents.getScene().getWindow();

            // Charger la nouvelle interface en arrière-plan
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumAjouterDocument.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene newScene = new Scene(root);

            // Appliquer la nouvelle scène à la fenêtre actuelle avant de la fermer
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(Documents document) {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) table_documents.getScene().getWindow();

            // Charger la nouvelle interface pour la modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumModifierDocument.fxml"));
            Parent root = loader.load();

            // Passer le document à la fenêtre de modification
            ModifierDocumentController controller = loader.getController();
            controller.setDocument(document, serviceDocument);

            // Créer une nouvelle scène
            Scene newScene = new Scene(root);

            // Appliquer la nouvelle scène à la fenêtre actuelle
            currentStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Fonction pour gérer une demande
    private void handleGerer(Demandes demande) {
        String status = demande.getStatut_demande();
        if ("nouvelle".equals(status)) {
            // Si la demande est "nouvelle", ajouter un document
            handleAjouterDocumentForDemande(demande);
        } else if ("en cours".equals(status)) {
            // Si la demande est "en cours", modifier le document
            handleModifyDocumentForDemande(demande);
        }
    }

    private void handleAjouterDocumentForDemande(Demandes demande) {
        try {
            // Charger l'interface d'ajout d'ID document
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterIdDocumentDemande.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer la demande sélectionnée
            ajouterIdDocument controller = loader.getController();
            controller.setDemande(demande, serviceDemande);

            // Création d'une nouvelle fenêtre modale
            Stage stage = new Stage();
            stage.setTitle("Ajouter ID Document à la Demande");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Afficher la fenêtre et attendre sa fermeture
            stage.showAndWait();

            // Recharger la liste des demandes après la modification
            loadDataDemandes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Fonction pour modifier le document d'une demande
    private void handleModifyDocumentForDemande(Demandes demande) {
        Documents document = serviceDocument.getDocumentForDemande(demande.getId_demande());
        if (document != null) {
            try {
                // Récupérer la fenêtre actuelle
                Stage currentStage = (Stage) ajouter_documents.getScene().getWindow();

                // Charger la nouvelle interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forumModifierDocument.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur et passer les données
                ModifierDocumentController controller = loader.getController();
                controller.setDocument(document, serviceDocument);

                // Créer et appliquer la nouvelle scène
                Scene newScene = new Scene(root);
                currentStage.setScene(newScene);

                // Mettre à jour les données après modification
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
            // Changer le statut à "validée"
            demande.setStatut_demande("validée");

            // Mettre à jour la demande dans la base de données
            serviceDemande.modifier(demande);

            // Afficher une confirmation (facultatif)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText(null);
            alert.setContentText("Le statut de la demande a été mis à jour en 'validée'.");
            alert.showAndWait();
        } else {
            // Afficher un message si la demande n'est pas "en cours"
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Action non autorisée");
            alert.setHeaderText(null);
            alert.setContentText("Le statut de la demande ne peut pas être modifié.");
            alert.showAndWait();
        }
    }



}
