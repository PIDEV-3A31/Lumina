package com.esprit.controllers.Transport;

import com.esprit.models.moyenTransport;
import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.serviceLigneTransport;
import com.esprit.services.serviceMoyenTransport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModifierMoyenTransport {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private ComboBox<String> ligneComboBox;
    @FXML
    private Label name_current_user;
    @FXML
    private ImageView img_current_user;

    @FXML
    private Button button_update;

    @FXML
    private TextField capaciteMax_label;

    @FXML
    private TextField etat_label;

    @FXML
    private TextField idLigne_label;

    @FXML
    private TextField immatriculation_label;

    @FXML
    private ImageView return_consulterMoyen;

    @FXML
    private TextField typeTransport_label;

    private moyenTransport currentMoyen;
    private final serviceMoyenTransport serviceMoyenTransport = new serviceMoyenTransport();

    private Map<Integer, String> lignesMap = new HashMap<>();
    private int selectedLigneId = -1;

    @FXML
    private Label immatriculationErrorLabel;
    @FXML
    private Label typeTransportErrorLabel;
    @FXML
    private Label etatErrorLabel;
    @FXML
    private Label capaciteMaxErrorLabel;
    @FXML
    private Label ligneTransportErrorLabel;
    @FXML
    private ImageView OpenChatBot;

    @FXML
    private void handleConsulterMoyenTransport() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) return_consulterMoyen.getScene().getWindow();

            // Charger la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulter_ligneTransport.fxml"));
            Parent root = loader.load();
            ConsulterLigneTransport controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            // Changer la scène actuelle
            Scene newScene = new Scene(root);
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
        chargerLignesTransport();
        System.out.println("Initialisation du contrôleur ModifierMoyenTransport");

        // Vérifier si les composants sont bien chargés
        if (button_update == null) {
            System.out.println(" ERREUR : button_update est NULL ! ");
        }

        // Associer l'événement au bouton
        button_update.setOnAction(event -> modifierMoyenTransport());

        return_consulterMoyen.setOnMouseClicked(event -> handleConsulterMoyenTransport());
    }

    // Méthode pour initialiser les valeurs du moyen de transport à modifier
    public void setMoyenTransport(moyenTransport moyen) {
        this.currentMoyen = moyen;

        // Remplir les champs avec les données du moyen de transport
        immatriculation_label.setText(moyen.getImmatriculation());
        typeTransport_label.setText(moyen.getTypeTransport());
        capaciteMax_label.setText(String.valueOf(moyen.getCapaciteMax()));
        etat_label.setText(moyen.getEtat());

        // Récupérer l'ID de la ligne associée
        int idLigneAssocie = moyen.getIdLigne();

        // Vérifier si cet ID existe dans la map
        if (lignesMap.containsKey(idLigneAssocie)) {
            String nomLigne = lignesMap.get(idLigneAssocie);
            ligneComboBox.setValue(nomLigne); // Sélectionner la ligne associée
            selectedLigneId = idLigneAssocie; // Mettre à jour l'ID sélectionné
        }
    }

    private void chargerLignesTransport() {
        serviceLigneTransport serviceLigne = new serviceLigneTransport();
        lignesMap = serviceLigne.getLignesMap(); // Récupérer les noms & IDs

        ligneComboBox.getItems().addAll(lignesMap.values()); // Ajouter les noms

        // Gérer la sélection pour récupérer l'ID correspondant
        ligneComboBox.setOnAction(event -> {
            String selectedNom = ligneComboBox.getValue();
            for (Map.Entry<Integer, String> entry : lignesMap.entrySet()) {
                if (entry.getValue().equals(selectedNom)) {
                    selectedLigneId = entry.getKey(); // Récupérer l'ID
                    break;
                }
            }
        });
    }


    @FXML
    private void modifierMoyenTransport() {
        String immatriculation = immatriculation_label.getText();
        String typeTransport = typeTransport_label.getText();
        String etat = etat_label.getText();
        int capaciteMax=0;


        immatriculationErrorLabel.setText("");
        typeTransportErrorLabel.setText("");
        etatErrorLabel.setText("");
        capaciteMaxErrorLabel.setText("");
        ligneTransportErrorLabel.setText("");
        // Vérifier si un ID de ligne a été sélectionné
        if (selectedLigneId == -1) {
            ligneTransportErrorLabel.setText("Veuillez sélectionner une ligne de transport !");
        }

        try {
            capaciteMax = Integer.parseInt(capaciteMax_label.getText());
        } catch (NumberFormatException e) {
            capaciteMaxErrorLabel.setText("Capacité Max doit être un nombre valide.");
        }


        boolean hasError = false;
        if (immatriculation.isEmpty()) {
            immatriculationErrorLabel.setText("Immatriculation est obligatoire.");
            hasError = true;
        }
        if (typeTransport.isEmpty()) {
            typeTransportErrorLabel.setText("Type de transport est obligatoire.");
            hasError = true;
        }
        if (etat.isEmpty()) {
            etatErrorLabel.setText("État est obligatoire.");
            hasError = true;
        }

        if (hasError) {
            return;
        }



        // Mise à jour du moyen de transport
        currentMoyen.setImmatriculation(immatriculation);
        currentMoyen.setTypeTransport(typeTransport);
        currentMoyen.setCapaciteMax(capaciteMax);
        currentMoyen.setEtat(etat);


        // Mise à jour dans la base de données
        try {
            serviceMoyenTransport.modifier(currentMoyen);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "✅ Moyen de transport modifié avec succès !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du moyen de transport.");
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
