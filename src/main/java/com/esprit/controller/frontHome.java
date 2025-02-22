package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Objects;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;
import java.util.List;

public class frontHome {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;
    @FXML
    private ImageView profile;
    @FXML
    private ImageView deconnexion;
    @FXML
    private ImageView to_home;

    @FXML
    private TextField search_field;
    @FXML
    private VBox searchResultsBox;
    @FXML
    private ScrollPane searchScrollPane;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label username;
    @FXML
    private ImageView parametres;

    @FXML
    public void initialize() {
        // Ajouter les gestionnaires d'événements
        profile.setOnMouseClicked(event -> navigateToProfile());
        deconnexion.setOnMouseClicked(event -> logout());
        to_home.setOnMouseClicked(event -> refreshHome());
        name_current_user.setOnMouseClicked(event -> navigateToProfile());

        // Ajouter le style de curseur pointer
        profile.setStyle("-fx-cursor: hand;");
        deconnexion.setStyle("-fx-cursor: hand;");
        to_home.setStyle("-fx-cursor: hand;");
        name_current_user.setStyle("-fx-cursor: hand;");
        parametres.setOnMouseClicked(event -> navigateToParametres());


    }

    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        updateUI();
    }

    private void updateUI() {
        name_current_user.setText(userProfile.getName_u());
        if (userProfile.getImage_u() != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                img_current_user.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
        loadImage(userProfile.getImage_u(), img_current_user);
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

    private void navigateToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAffichProf.fxml"));
            Parent root = loader.load();

            frontAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) profile.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_home.fxml"));
            Parent root = loader.load();

            frontHome controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) to_home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    private void navigateToParametres() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Parametres.fxml"));
            Parent root = loader.load();

            Parametres controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) parametres.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void initializeSearch() {
        // Cacher initialement la boîte de résultats
        searchResultsBox.setVisible(false);
        searchResultsBox.setManaged(false);

        // Ajouter le listener pour la recherche dynamique
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                searchResultsBox.getChildren().clear();
                searchResultsBox.setVisible(false);
                searchResultsBox.setManaged(false);
                return;
            }

            // Effectuer la recherche
            ServiceProfile serviceProfile = new ServiceProfile();
            ServiceUser serviceUser = new ServiceUser();
            List<profile> profiles = serviceProfile.afficher();

            // Filtrer les résultats
            searchResultsBox.getChildren().clear();
            for (profile p : profiles) {
                user u = serviceUser.getUserById(p.getId_user());
                if (u.getUsername().toLowerCase().contains(newValue.toLowerCase())) {
                    // Créer un élément de résultat
                    HBox resultItem = createSearchResultItem(u, p);
                    searchResultsBox.getChildren().add(resultItem);
                }
            }

            // Afficher les résultats
            searchResultsBox.setVisible(!searchResultsBox.getChildren().isEmpty());
            searchResultsBox.setManaged(!searchResultsBox.getChildren().isEmpty());
        });

        // Fermer les résultats quand on clique ailleurs
        search_field.getScene().setOnMouseClicked(event -> {
            if (!search_field.isHover() && !searchResultsBox.isHover()) {
                searchResultsBox.setVisible(false);
                searchResultsBox.setManaged(false);
            }
        });
    }

    private HBox createSearchResultItem(user u, profile p) {
        HBox item = new HBox(10); // 10 pixels de spacing
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(5, 10, 5, 10));
        item.getStyleClass().add("search-result-item");

        // Image de profil

        profilePic.setFitHeight(40);
        profilePic.setFitWidth(40);
        if (p.getImage_u() != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + p.getImage_u())).toExternalForm());
                profilePic.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }

        // Username
        username.setText(u.getUsername());
        username.getStyleClass().add("search-result-username");

        item.getChildren().addAll(profilePic, username);

        // Ajouter l'événement de clic

        if(item != null) {
            item.setOnMouseClicked(event -> {
                navigateToUserProfile(u, p);
            });
        }

        return item;
    }

    private void navigateToUserProfile(user selectedUser, profile selectedProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAffichAutreProf.fxml"));
            Parent root = loader.load();

            FrontAffichAutreProf controller = loader.getController();
            controller.initData(connectedUser, userProfile, selectedUser, selectedProfile);

            Stage stage = (Stage) search_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
