package com.esprit.controllers;

import com.esprit.controllers.Transport.InterfaceUserTransport;
import com.esprit.controllers.User.frontAffichProf;
import com.esprit.models.profile;
import com.esprit.models.user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class interfaceHome {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Pane button_municipality;

    @FXML
    private Pane button_transport;

    @FXML
    private Pane button_traffic;
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
        // Action associée au pane "pane"
        button_transport.setOnMouseClicked(event -> {
            navigateToTransport();

        });
        button_municipality.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/fxml/documents_user.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button_traffic.setOnMouseClicked(event -> {
            try {
                handleRedirection(event, "/gestion_traffic.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void handleRedirection(MouseEvent event, String fxmlPath) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));


        // Récupérer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Changer la scène
        stage.setScene(scene);
        stage.show();
    }
    private void handleRedirectionTransport(MouseEvent event, String fxmlPath) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        System.out.println("test");
        InterfaceUserTransport controller = loader.getController();
        controller.initData(connectedUser, userProfile);


        // Récupérer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Changer la scène
        stage.setScene(scene);
        stage.show();
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
    private void navigateToTransport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface_user_transport.fxml"));
            Parent root = loader.load();
            System.out.println("testfrom");
            InterfaceUserTransport controller = loader.getController();
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

}