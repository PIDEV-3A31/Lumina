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
import com.esprit.utils.NavigationHistory;

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
    private Button deconnexion;

    @FXML
    public void initialize() {
        columnID_user.setCellValueFactory(new PropertyValueFactory<>("id_profile"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("name_u"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

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

        loadTableData();

        suppBtn.setOnAction(event -> Delete());

        editBtn.setOnAction(event -> Edit());

        ajouter_user.setOnAction(event -> Add());

        name_current_user.setOnMouseClicked(event -> editCurrentUserProfile());
        
        name_current_user.setStyle("-fx-cursor: hand;");

        deconnexion.setOnAction(event -> logout());
    }

    public void initData(user user, profile profile) {
        this.connectedUser = user;
        this.userProfile = profile;
        updateUI();
    }

    private void updateUI() {
        if (userProfile != null) {
            name_current_user.setText(userProfile.getName_u());
            
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
            ServiceUser serviceUser = new ServiceUser();
            user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());

            username_userselectionne.setText(selectedUser.getUsername());
            name_userselectionne.setText(selectedProfile.getName_u());
            email_userselectionne.setText(selectedProfile.getEmail_u());
            phone_userselectionne.setText(String.valueOf(selectedProfile.getPhone_u()));
            role_userselectionne.setText(selectedProfile.getRole());
            
            iduser_selectionne.setText(String.valueOf(selectedProfile.getId_user()));
            idprofil_userselectionne.setText(String.valueOf(selectedProfile.getId_profile()));

            if (selectedProfile.getCreated_at() != null) {
                createdat_userselectionne.setText(selectedProfile.getCreated_at().toString());
            }
            if (selectedProfile.getUpdated_at() != null) {
                updatedat_userselectionne.setText(selectedProfile.getUpdated_at().toString());
            }

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
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.supprimer(selectedProfile.getId_user());

                loadTableData();
                
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
            System.out.println(getClass().getResource("/dashboardModifProf.fxml"));
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

    /*private void navigateBack() {
        try {
            String previousPage = NavigationHistory.popPage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(previousPage));
            Parent root = loader.load();
            
            // Initialiser le contrôleur selon la page
            Object controller = loader.getController();
            if (controller instanceof dashboardController) {
                ((dashboardController) controller).initData(connectedUser, userProfile);
            } else if (controller instanceof dashboardAffichProf) {
                ((dashboardAffichProf) controller).initData(connectedUser, userProfile);
            }
            // ... autres cas selon vos contrôleurs

            Stage stage = (Stage) retourBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    public void refreshTableView() {
        ServiceProfile serviceProfile = new ServiceProfile();
        ObservableList<profile> profiles = FXCollections.observableArrayList(serviceProfile.afficher());
        tableView.setItems(profiles);
        tableView.refresh();
    }
    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + imagePath)).toExternalForm());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }

    private void handleUserSelection(profile selectedProfile) {
        if (selectedProfile != null) {
            // ... code existant ...
            loadImage(selectedProfile.getImage_u(), image_userselectionne);
        }
    }
}
