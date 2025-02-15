package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class dashboardModifProf {
    private user connectedUser;
    private profile userProfile;
    private profile selectedProfile;
    private boolean isAddMode = false;

    @FXML
    private Label name_userconnecte;
    @FXML
    private TextField modifusername;
    @FXML
    private TextField modifemail;
    @FXML
    private PasswordField modifpassword;
    @FXML
    private TextField modifname;
    @FXML
    private TextField modifphone;
    @FXML
    private ComboBox<String> modifrole;
    @FXML
    private Button Save;
    @FXML
    private Label identfLabel;

    @FXML
    public void initialize() {
        Save.setOnAction(event -> handleUpdate());
    }

    public void initData(user connectedUser, profile userProfile, profile selectedProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.selectedProfile = selectedProfile;
        this.isAddMode = false;

        // Afficher le nom de l'utilisateur connecté
        name_userconnecte.setText(userProfile.getName_u());

        // Remplir les champs avec les données de l'utilisateur sélectionné
        ServiceUser serviceUser = new ServiceUser();
        user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());

        modifusername.setText(selectedUser.getUsername());
        modifpassword.setText(selectedUser.getPassword());
        modifname.setText(selectedProfile.getName_u());
        modifemail.setText(selectedProfile.getEmail_u());
        modifphone.setText(String.valueOf(selectedProfile.getPhone_u()));
        modifrole.setValue(selectedProfile.getRole());
        Save.setText("Save Changes");
        identfLabel.setText("Modify Account Details");
    }

    public void initDataForAdd(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.isAddMode = true;

        // Afficher le nom de l'utilisateur connecté
        name_userconnecte.setText(userProfile.getName_u());

        // Vider tous les champs pour l'ajout
        modifusername.setText("");
        modifpassword.setText("");
        modifname.setText("");
        modifemail.setText("");
        modifphone.setText("");
        modifrole.setValue(null);

        // Changer le texte du bouton
        Save.setText("Add User");
        identfLabel.setText("Add New Account");

    }

    private void handleUpdate() {
        if (!validateInputs()) {
            return;
        }

        try {
            if (isAddMode) {
                // Créer un nouvel utilisateur
                user newUser = new user(modifusername.getText(), modifpassword.getText());
                ServiceUser serviceUser = new ServiceUser();
                int userId = serviceUser.ajouterAvecId(newUser);

                if (userId == -1) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création de l'utilisateur!");
                    return;
                }

                // Créer un nouveau profil
                profile newProfile = new profile(
                    userId,
                    modifname.getText(),
                    modifemail.getText(),
                    Integer.parseInt(modifphone.getText()),
                    modifrole.getValue()
                );
                ServiceProfile serviceProfile = new ServiceProfile();
                serviceProfile.ajouter(newProfile);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            } else {
                // Mettre à jour l'utilisateur
                user updatedUser = new user(selectedProfile.getId_user(), 
                    modifusername.getText(), modifpassword.getText());
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.modifer(updatedUser, selectedProfile.getId_user());

                // Mettre à jour le profil
                profile updatedProfile = new profile(
                    selectedProfile.getId_user(),
                    selectedProfile.getId_profile(),
                    modifname.getText(),
                    modifemail.getText(),
                    Integer.parseInt(modifphone.getText()),
                    modifrole.getValue()
                );
                ServiceProfile serviceProfile = new ServiceProfile();
                serviceProfile.modifer(updatedProfile, selectedProfile.getId_profile());

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Modifications enregistrées avec succès!");
            }
            navigateToDashboard();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur l'" + (isAddMode ? "ajout" : "modification") + ": " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        // Vérification des champs vides
        if (modifusername.getText().isEmpty() || modifemail.getText().isEmpty() || 
            modifpassword.getText().isEmpty() || modifname.getText().isEmpty() || 
            modifphone.getText().isEmpty() || modifrole.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs!");
            return false;
        }

        // Vérification de la longueur du username
        if (modifusername.getText().length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Username must not exceed 20 characters!");
            return false;
        }

        // Vérification de la longueur du nom
        if (modifname.getText().length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Name must not exceed 25 characters!");
            return false;
        }

        // Vérification du numéro de téléphone (8 chiffres)
        if (!modifphone.getText().matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Phone number must be exactly 8 digits!");
            return false;
        }

        // Validation de l'email
        if (!modifemail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Please enter a valid email address!");
            return false;
        }

        return true;
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            dashboardController dashCtrl = loader.getController();
            dashCtrl.initData(connectedUser, userProfile);

            Stage stage = (Stage) Save.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
