package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
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
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleObjectProperty;

public class dashboardController {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_current_user;

    @FXML
    private TableView<profile> tableView;
    @FXML
    private TableColumn<profile, Integer> columnID_user;
    @FXML
    private TableColumn<profile, String> columnUsername;
    @FXML
    private TableColumn<profile, String> columnRole;

    // Labels pour les détails de l'utilisateur sélectionné
    @FXML
    private Label username_userselectionne;
    @FXML
    private Label name_userselectionne;
    @FXML
    private Label email_userselectionne;
    @FXML
    private Label phone_userselectionne;
    @FXML
    private Label role_userselectionne;
    @FXML
    private Label createdat_userselectionne;
    @FXML
    private Label updatedat_userselectionne;
    @FXML
    private Label iduser_selectionne;
    @FXML
    private Label idprofil_userselectionne;
    @FXML
    private TableColumn<profile, ImageView> columnImage;

    @FXML
    private Button suppBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button ajouter_user;

    @FXML
    private ImageView img_current_user;
    @FXML
    private ImageView image_userselectionne;

    @FXML
    public void initialize() {
        // Initialiser les colonnes de la TableView
        columnID_user.setCellValueFactory(new PropertyValueFactory<>("id_profile"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("name_u"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Configurer la colonne image
        columnImage.setCellValueFactory(param -> {
            profile p = param.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            
            if (p.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + p.getImage_u())).toExternalForm());
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }
            
            return new SimpleObjectProperty<>(imageView);
        });

        // Ajouter un listener pour la sélection dans la TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showUserDetails(newSelection);
            }
        });

        // Charger les données
        loadTableData();

        // Ajouter le gestionnaire d'événement pour le bouton supprimer
        suppBtn.setOnAction(event -> Delete());

        // Ajouter le gestionnaire d'événement pour le bouton modifier
        editBtn.setOnAction(event -> Edit());

        // Ajouter le gestionnaire d'événement pour le bouton ajouter
        ajouter_user.setOnAction(event -> Add());

        // Ajouter le gestionnaire d'événement pour le nom de l'utilisateur connecté
        name_current_user.setOnMouseClicked(event -> editCurrentUserProfile());
        
        // Pour indiquer visuellement que c'est cliquable
        name_current_user.setStyle("-fx-cursor: hand;");
    }

    public void initData(user user, profile profile) {
        this.connectedUser = user;
        this.userProfile = profile;
        updateUI();
    }

    private void updateUI() {
        if (userProfile != null) {
            name_current_user.setText(userProfile.getName_u());
            
            // Charger l'image de l'utilisateur connecté
            if (userProfile.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                    img_current_user.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }
        }
    }

    private void loadTableData() {
        ServiceProfile serviceProfile = new ServiceProfile();
        ObservableList<profile> profiles = FXCollections.observableArrayList(serviceProfile.afficher());
        tableView.setItems(profiles);
    }

    private void showUserDetails(profile selectedProfile) {
        if (selectedProfile != null) {
            // Récupérer l'utilisateur correspondant
            ServiceUser serviceUser = new ServiceUser();
            user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());

            // Afficher les informations de base
            username_userselectionne.setText(selectedUser.getUsername());
            name_userselectionne.setText(selectedProfile.getName_u());
            email_userselectionne.setText(selectedProfile.getEmail_u());
            phone_userselectionne.setText(String.valueOf(selectedProfile.getPhone_u()));
            role_userselectionne.setText(selectedProfile.getRole());
            
            // Afficher les IDs
            iduser_selectionne.setText(String.valueOf(selectedProfile.getId_user()));
            idprofil_userselectionne.setText(String.valueOf(selectedProfile.getId_profile()));

            // Afficher les timestamps
            if (selectedProfile.getCreated_at() != null) {
                createdat_userselectionne.setText(selectedProfile.getCreated_at().toString());
            }
            if (selectedProfile.getUpdated_at() != null) {
                updatedat_userselectionne.setText(selectedProfile.getUpdated_at().toString());
            }

            // Afficher l'image
            if (selectedProfile.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + selectedProfile.getImage_u())).toExternalForm());
                    image_userselectionne.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }
        }
    }

    private void Delete() {
        profile selectedProfile = tableView.getSelectionModel().getSelectedItem();
        if (selectedProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur à supprimer!");
            return;
        }

        // Demander confirmation avant la suppression
        Optional<ButtonType> result = showConfirmation("Confirmation", 
            "Êtes-vous sûr de vouloir supprimer cet utilisateur ?",
            "Cette action ne peut pas être annulée.");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer l'utilisateur (la suppression en cascade s'occupera du profil)
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.supprimer(selectedProfile.getId_user());

                // Rafraîchir la TableView
                loadTableData();
                
                // Effacer les détails de l'utilisateur
                clearUserDetails();
                
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de la suppression : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Optional<ButtonType> showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearUserDetails() {
        username_userselectionne.setText("");
        name_userselectionne.setText("");
        email_userselectionne.setText("");
        phone_userselectionne.setText("");
        role_userselectionne.setText("");
        createdat_userselectionne.setText("");
        updatedat_userselectionne.setText("");
        iduser_selectionne.setText("");
        idprofil_userselectionne.setText("");
    }

    private void Edit() {
        profile selectedProfile = tableView.getSelectionModel().getSelectedItem();
        if (selectedProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur à modifier!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifProf.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et initialiser les données
            dashboardModifProf modifController = loader.getController();
            modifController.initData(connectedUser, userProfile, selectedProfile);

            Stage stage = (Stage) editBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void Add() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifProf.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et initialiser pour l'ajout
            dashboardModifProf modifController = loader.getController();
            modifController.initDataForAdd(connectedUser, userProfile);

            Stage stage = (Stage) ajouter_user.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void editCurrentUserProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
            Parent root = loader.load();

            dashboardAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) name_current_user.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }
}
