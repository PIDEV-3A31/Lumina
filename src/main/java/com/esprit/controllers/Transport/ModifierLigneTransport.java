package com.esprit.controllers.Transport;

import com.esprit.models.ligneTransport;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.serviceLigneTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;
import java.util.Objects;

public class ModifierLigneTransport {
    private user connectedUser;
    private profile userProfile;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

    @FXML
    private Button button_modifier;

    @FXML
    private TextField nomLigne_label;

    @FXML
    private TextField zoneCouverture_label;

    @FXML
    private TextField tarif_label;

    @FXML
    private TextField etat_label;

    @FXML
    private ComboBox<Integer> hourDepartComboBox;

    @FXML
    private ComboBox<Integer> minuteDepartComboBox;

    @FXML
    private ComboBox<Integer> hourArriveeComboBox;

    @FXML
    private ComboBox<Integer> minuteArriveeComboBox;

    @FXML
    private TextField lieuxArrive_label;

    @FXML
    private TextField lieuxDepart_label;

    @FXML
    private ImageView return_consulterLigne;

    @FXML
    private Label errorEtat;

    @FXML
    private Label errorLieuxArrive;

    @FXML
    private Label errorLieuxDepart;

    @FXML
    private Label errorNomLigne;

    @FXML
    private Label errorTarif;

    @FXML
    private Label errorZoneCouverture;

    @FXML
    private ImageView OpenChatBot;


    private ligneTransport currentLigne;
    private final serviceLigneTransport serviceLigneTransport = new serviceLigneTransport();

    @FXML
    private void handleConsulterTransport() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) return_consulterLigne.getScene().getWindow();

            // Charger la nouvelle interface en arrière-plan
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();
            ConsulterLigneTransport controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            // Créer une nouvelle scène
            Scene newScene = new Scene(root);

            // Appliquer la nouvelle scène à la fenêtre actuelle avant de la fermer
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openChatBotWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatbotUi.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("ChatBot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        OpenChatBot.setOnMouseClicked(event -> openChatBotWindow());
        System.out.println("Initialisation du contrôleur ModifierLigneTransport");

        // Vérifier si les composants sont bien chargés
        if (button_modifier == null) {
            System.out.println(" ERREUR : button_modifier est NULL ! ");
        }
        if (hourDepartComboBox == null || minuteDepartComboBox == null ||
                hourArriveeComboBox == null || minuteArriveeComboBox == null) {
            System.out.println(" ERREUR : Une ComboBox est NULL ! ");
            return;
        }

        // Remplir les ComboBox avec les heures et minutes
        for (int i = 0; i <= 23; i++) {
            hourDepartComboBox.getItems().add(i);
            hourArriveeComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            minuteDepartComboBox.getItems().add(i);
            minuteArriveeComboBox.getItems().add(i);
        }

        // Définir des valeurs par défaut
        hourDepartComboBox.setValue(0);
        minuteDepartComboBox.setValue(0);
        hourArriveeComboBox.setValue(0);
        minuteArriveeComboBox.setValue(0);

        // Associer l'événement au bouton
        button_modifier.setOnAction(event -> modifierLigneTransport());

        return_consulterLigne.setOnMouseClicked(event -> { handleConsulterTransport(); });



    }

    // Méthode pour initialiser les valeurs de la ligne à modifier
    public void setLigneTransport(ligneTransport ligne) {
        this.currentLigne = ligne;

        // Remplir les champs avec les données de la ligne
        nomLigne_label.setText(ligne.getNomLigne());
        zoneCouverture_label.setText(ligne.getZoneCouverture());
        tarif_label.setText(String.valueOf(ligne.getTarif()));
        etat_label.setText(ligne.getEtat());
        lieuxDepart_label.setText(ligne.getLieuxDepart());
        lieuxArrive_label.setText(ligne.getLieuxArrive());

        // Remplir les ComboBox avec les heures et minutes de départ et d'arrivée
        hourDepartComboBox.setValue(ligne.getHoraireDepart().toLocalTime().getHour());
        minuteDepartComboBox.setValue(ligne.getHoraireDepart().toLocalTime().getMinute());
        hourArriveeComboBox.setValue(ligne.getHoraireArrivee().toLocalTime().getHour());
        minuteArriveeComboBox.setValue(ligne.getHoraireArrivee().toLocalTime().getMinute());
    }

    @FXML
    private void modifierLigneTransport() {
        String nomLigne = nomLigne_label.getText();
        String zoneCouverture = zoneCouverture_label.getText();
        String etat = etat_label.getText();
        String lieuxDepart = lieuxDepart_label.getText();
        String lieuxArrive = lieuxArrive_label.getText();

        errorNomLigne.setText("");
        errorZoneCouverture.setText("");
        errorEtat.setText("");
        errorLieuxDepart.setText("");
        errorLieuxArrive.setText("");
        errorTarif.setText("");


        boolean hasError = false;
        double tarif=0 ;
        if (nomLigne.isEmpty()) {
            errorNomLigne.setText("Le nom de la ligne est obligatoire !");
            hasError = true;
        }

        if (zoneCouverture.isEmpty()) {
            errorZoneCouverture.setText("Veuillez renseigner la zone de couverture !");
            hasError = true;
        }

        if (etat.isEmpty()) {
            errorEtat.setText("Veuillez indiquer l'état de la ligne !");
            hasError = true;
        }

        if (lieuxDepart.isEmpty()) {
            errorLieuxDepart.setText("Le lieu de départ est requis !");
            hasError = true;
        }

        if (lieuxArrive.isEmpty()) {
            errorLieuxArrive.setText("Le lieu d'arrivée est requis !");
            hasError = true;
        }

        try {
            tarif = Double.parseDouble(tarif_label.getText().trim());
            if (tarif < 0) {
                errorTarif.setText("Le tarif doit être positif !");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            errorTarif.setText("Le tarif doit être un nombre valide !");
            hasError = true;
        }

        if (hasError) {
            return;
        }






        // Récupérer les valeurs des ComboBox
        int hourDepart = hourDepartComboBox.getValue();
        int minuteDepart = minuteDepartComboBox.getValue();
        int hourArrivee = hourArriveeComboBox.getValue();
        int minuteArrivee = minuteArriveeComboBox.getValue();

        // Convertir en format Time
        Time horaireDepart = Time.valueOf(String.format("%02d:%02d:00", hourDepart, minuteDepart));
        Time horaireArrivee = Time.valueOf(String.format("%02d:%02d:00", hourArrivee, minuteArrivee));

        // Mise à jour de la ligne de transport
        currentLigne.setNomLigne(nomLigne);
        currentLigne.setZoneCouverture(zoneCouverture);
        currentLigne.setTarif(tarif);
        currentLigne.setEtat(etat);
        currentLigne.setHoraireDepart(horaireDepart);
        currentLigne.setHoraireArrivee(horaireArrivee);

        // Mise à jour dans la base de données
        try {
            serviceLigneTransport.modifier(currentLigne);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Ligne de transport modifiée avec succès !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la modification de la ligne de transport.");
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void initData(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        System.out.println("jesuisla");
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


}
